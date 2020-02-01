package Lesson04.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");

    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("Ivan");
    private final JPasswordField tfPassword = new JPasswordField("123");

    private final JButton btnLogin = new JButton("Login");
    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");


    private final JList<String> userList = new JList<>();//создадим список чтоб хранить список пользователей
    private boolean shownIoErrors = false;


    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//по центру экрана
        setSize(WIDTH, HEIGHT);
//        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);//создали скрол панель и добавили в него лог(куда пишем сообщения)
        JScrollPane scrollUser = new JScrollPane(userList);//скрол панель для пользователей и добавили в нее список с пользователями
        String[] users = {"user1", "user2", "user3", "user4", "user5"};//создали массив пользователей
        userList.setListData(users);//добавили в наш список-массив с пользователями
        scrollUser.setPreferredSize(new Dimension(100, 0));
        cbAlwaysOnTop.addActionListener(this::actionPerformed);
        tfMessage.addActionListener(this::actionPerformed);
        btnSend.addActionListener(this::actionPerformed);

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
        } else
            throw new RuntimeException("Unknown sourse: " + src);
    }

    private void sendMessage() {
        String msg = tfMessage.getText();//достаем сообщение которое было написано
        String username = tfLogin.getText();//и юсернейм
        if ("".equals(msg)) return;          //сравниваем если сообщение пустое то ничего не делаем-выход из метода
        tfMessage.setText(null);             //
        tfMessage.requestFocusInWindow();   //возвращаем фокус к текстфилду
        putLog(String.format("%s: %s", username, msg));//кладем сообщение  в лог в окошко
        wrtMsgToLogFile(msg, username);          //кладем его же в файл
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
}
