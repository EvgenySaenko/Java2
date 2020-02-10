package chat_server.core;


import chat_common.Library;
import network.ServerSocketThread;
import network.ServerSocketThreadListener;
import network.SocketThread;
import network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class ChatServer implements ServerSocketThreadListener, SocketThreadListener {

    private final ChatServerListener listener;
    private ServerSocketThread server;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private Vector<SocketThread> clients = new Vector<>();

    public ChatServer(ChatServerListener listener){
        this.listener = listener;
    }




    public void start(int port) {
        if (server != null && server.isAlive())
            putLog("Server already started");
        else
            server = new ServerSocketThread(this,"Server", port,2000);
    }

    public void stop() {
        if (server == null || !server.isAlive()) {
            putLog("Server is not running");
        } else {
            server.interrupt();
        }
    }

    private void putLog(String msg){
        msg = DATE_FORMAT.format(System.currentTimeMillis()) + Thread.currentThread().getName() + ": "+msg;
       listener.onChatServerMessage(msg);
    }

    /**
     * Server methods
     *
     * */
    //при стопе этот тред интерапт(прерывается), перестаем принимать новые сокеты
    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
        SqlClient.connect();
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
        SqlClient.disconnect();
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Server socket created");
    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {
//        putLog("Server timeout");
    }

    @Override//подсоединяемся к клиенту и прилетает готовый сокет сюда
    //и мы на его основе создаем новый поток(оборачиваем сокет в поток) оставив только взаимосвязь
    public void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client connected");
        String name = "SocketThread "+ socket.getInetAddress() + ":"+socket.getPort();
        new ClientThread(this,name,socket);//слушатель,имя, и сам сокет который нужно обернуть в поток
    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable exception) {
        exception.printStackTrace();
    }

    /**
     * Socket methods
     *
     * */

    @Override
    public synchronized void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Socket created");
    }

    @Override//удаляем клиента
    public synchronized void onSocketStop(SocketThread thread) {
        clients.remove(thread);
    }

    @Override//добавляем клиента
    public synchronized void onSocketReady(SocketThread thread, Socket socket) {
        clients.add(thread);
    }

    @Override//метод где получаем сообщение - рассылаем всем клиентам что такой то клиент активен
    public synchronized void onReceiveString(SocketThread thread, Socket socket, String msg) {
        ClientThread client =(ClientThread) thread;
        if (client.isAuthorized())//если не авторизован
            handleAuthMessage(client,msg);  //сообщения авторизованных
        else
            handleNonAuthMessage(client,msg);//сообщения не авторизованых
    }

    private void handleNonAuthMessage(ClientThread client, String msg){
        String arr[] = msg.split(Library.DELIMITER);
        if (arr.length != 3 || !arr[0].equals(Library.AUTH_REQUEST)){
            client.msgFormatError(msg);
            return;
        }
        String login = arr[1];
        String password = arr[2];
        String nickname = SqlClient.getNickname(login,password);
        if (nickname == null){
            putLog("Invalid login attempt: "+ login);
            client.authFail();
            return;
        }
        client.authAccept(nickname);
        sendToAuthClients(Library.getTypeBroadcast("Server ",nickname + " connected"));
    }
    //сообще авторизован польз
    private void handleAuthMessage(ClientThread client, String msg){
        sendToAuthClients(msg);
    }
    //можем посылать всем авторизованным клиентам(только авторизованым)
    private void sendToAuthClients(String msg) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            client.sendMessage(msg);
        }
    }

    @Override
    public synchronized void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();
    }
}