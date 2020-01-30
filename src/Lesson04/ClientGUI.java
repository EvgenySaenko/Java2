package Lesson04;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
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

    private static FileWriter logMessage;

    private final JList<String> userList = new JList<>();//создадим список чтоб хранить список пользователей


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
    //создадим метод запись сообщения в логчата(дублирующий запись в текстовый файл)
    public void recordInLog(JTextField tf){
        String msg = tf.getText();
        log.append(msg + "\n");
        try {
            logMessage = new FileWriter("logMessage.txt", true);
            logMessage.write("\n"+ userList.getName()+": "+msg);
            logMessage.flush();
            logMessage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        tf.setText(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            if (src == cbAlwaysOnTop) {
                setAlwaysOnTop(cbAlwaysOnTop.isSelected());
            } else {
                throw new RuntimeException("Unknown sourse: " + src);
            }
        }
        if (src == tfMessage || src == btnSend) {
//            System.out.println("Сообщение "+ tfMessage.getText());
            recordInLog(tfMessage);
        }

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();//создаем массив элементов и достаем его из Стэк трейса
        msg = "Exeption in " + t.getName() + " " + e.getClass().getCanonicalName() + ": " + e.getMessage() + " \n\t at " + ste[0];
        //JOptionPane - позволяет выкидывать окошки, отрисовываемся на основе нас(this),
        // сообщение наше,тайтл,и выбираем вид панельки которая будет выкидываться ее вид
        JOptionPane.showMessageDialog(this, msg, "Exeption", JOptionPane.ERROR_MESSAGE);
        System.exit(1);//ставим выход из системы после как пользователь нажмет на "ок" прочитав панель ошибки-
        // программа свернется по (1)- то есть екстренно, при нормальном (0)
    }
}
