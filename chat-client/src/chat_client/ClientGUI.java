package chat_client;

import chat_common.Library;
import network.SocketThread;
import network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");

    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("EvgS");
    private final JPasswordField tfPassword = new JPasswordField("123");

    private final JButton btnLogin = new JButton("Login");
    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");


    private final JList<String> userList = new JList<>();//создадим список чтоб хранить список пользователей
    private boolean shownIoErrors = false;
    private SocketThread socketThread;


    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//по центру экрана
        setSize(WIDTH, HEIGHT);
        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane scrollLog = new JScrollPane(log);//создали скрол панель и добавили в него лог(куда пишем сообщения)
        JScrollPane scrollUser = new JScrollPane(userList);//скрол панель для пользователей и добавили в нее список с пользователями
        String[] users = {"user1", "user2", "user3", "user4", "user5"};//создали массив пользователей
        userList.setListData(users);//добавили в наш список-массив с пользователями
        scrollUser.setPreferredSize(new Dimension(100, 0));
        cbAlwaysOnTop.addActionListener(this::actionPerformed);
        tfMessage.addActionListener(this::actionPerformed);
        btnSend.addActionListener(this::actionPerformed);
        btnLogin.addActionListener(this::actionPerformed);
        btnDisconnect.addActionListener(this::actionPerformed);
        panelBottom.setVisible(false);//

        panelTop.add(tfIPAddress);//на верхнюю панель добавляем текст адрес ИП, Порт,Алвейсонтоп
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);

        panelTop.add(tfLogin);//так же добавляем логин пароль текст и кнопку -нажать залогиниться
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);

        panelBottom.add(btnDisconnect, BorderLayout.WEST);//на нижнюю панельку кнопку дисконект,
        panelBottom.add(tfMessage, BorderLayout.CENTER);//поля текста с сообщение текущим которое будет вводить пользователь
        panelBottom.add(btnSend, BorderLayout.EAST);//и кнопку отправки сообщения

        add(scrollLog, BorderLayout.CENTER);//добавили скрол панель
        add(scrollUser, BorderLayout.EAST);//добавили скрол панель справа
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void connect(){
        try {//создаем сокет даем адрес сервера и порт
            Socket socket = new Socket(tfIPAddress.getText(),Integer.parseInt(tfPort.getText()));
            socketThread = new SocketThread(this,"Client",socket);
        } catch (IOException e) {
            showException(Thread.currentThread(),e);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {//Event Dispatching Thread - поток управляющий событиями
            @Override                            //все что связано со swing должно выполняться в этом потоке
            public void run() {
                new ClientGUI();//вызываем конструктор ServerGUI
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == tfMessage || src == btnSend) {
            sendMessage();
        } else if (src == btnLogin){
            connect();
        } else if (src == btnDisconnect){
            socketThread.close();
        }else {
            throw new RuntimeException("Unknown sourse: " + src);
        }
    }

    private void sendMessage() {
        String msg = tfMessage.getText();//достаем сообщение которое было написано
        String username = tfLogin.getText();//и юсернейм
        if ("".equals(msg)) return;          //сравниваем если сообщение пустое то ничего не делаем-выход из метода
        tfMessage.setText(null);             //
        tfMessage.requestFocusInWindow();   //возвращаем фокус к текстфилду
        socketThread.sendMessage(msg);
        //wrtMsgToLogFile(msg, username);          //кладем его же в файл
    }


    private void wrtMsgToLogFile(String msg, String username) {
        //в трай с ресурсами (он принимает некий объект класса который реализует интерфейс клоузебл)
        // поток открывается записывает файл и трай с ресурсами автоматически закрывает поток
        try (FileWriter out = new FileWriter("logMessage.txt", true)) {
            out.write(username + ": " + msg + "\n");
            out.flush();        //выполняет все чтобыло в буфере и очищает его
        } catch (IOException e) {
            if (!shownIoErrors) {
                shownIoErrors = true;
                showException(Thread.currentThread(), e);
            }
        }
    }


    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }


    //метод выводит ошибку пользователю
    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();//создаем массив элементов и достаем его из Стэк трейса
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = "Exception in " + t.getName() + " " + e.getClass().getCanonicalName() + ": " +
                    e.getMessage() + "\n\t at " + ste[0];
            //JOptionPane - позволяет выкидывать окошки, отрисовываемся на основе нас(this),
            // сообщение наше,тайтл,и выбираем вид панельки которая будет выкидываться ее вид
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        showException(t, e);
        System.exit(1);//ставим выход из системы после как пользователь нажмет на "ок" прочитав панель ошибки-
        // программа свернется по (1)- то есть екстренно, при нормальном (0)
    }

    /**
     * Socket Thread Listener methods
     * */

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Start");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        panelBottom.setVisible(false);
        panelTop.setVisible(true);
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        panelBottom.setVisible(true);
        panelTop.setVisible(false);
        String login = tfLogin.getText();
        String password = new String (tfPassword.getPassword());
        thread.sendMessage(Library.getAuthRequest(login,password));
    }

    @Override//получаем от сервака что кидать в лог
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        putLog(msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        //showException(thread,exception);
    }
}
