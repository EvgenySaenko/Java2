package chat_server.core;

import chat_common.Library;
import network.SocketThread;
import network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {

    private String nickname;
    private boolean isAuthorized;

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isAuthorized(){
        return isAuthorized;
    }

    public void authAccept(String nickname){
        isAuthorized = true;
        this.nickname = nickname;
        sendMessage(Library.getAuthAccept(nickname));//шлем сообщение клиенту об авторизации
    }

    public void  authFail(){
        sendMessage(Library.getAuthDenied());
        close();  //закрываем сокет с нашей стороны
    }
    public void msgFormatError(String msg){
        sendMessage(Library.getMsgFormatError(msg));
        close();
    }




}
