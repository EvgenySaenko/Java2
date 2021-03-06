package Lesson04.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//наш интерфейс
public class ServerGUI extends JFrame implements ActionListener,Thread.UncaughtExceptionHandler{

    private static final int POS_X = 1000;
    private static final int POS_Y = 550;
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;

    private final ChatServer chatServer = new ChatServer();//создали чат сервер
    private final JButton btnStart = new JButton("Start");//кнопку старт и стоп
    private final JButton btnStop = new JButton("Stop");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {//Event Dispatching Thread - поток управляющий событиями
            @Override                            //все что связано со swing должно выполняться в этом потоке
            public void run() {
                new ServerGUI();//вызываем конструктор ServerGUI
            }
        });
    }

    private ServerGUI(){
        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X,POS_Y,WIDTH,HEIGHT); //установили границы окошка
        setResizable(false);                 //ставим невозможным изменить размеры нашего окна
        setTitle("Chat server");
        setAlwaysOnTop(true);   //поверх всех окон
        setLayout(new GridLayout(1,2));//добавили компоновщик
        btnStart.addActionListener(this::actionPerformed);
        btnStop.addActionListener(this::actionPerformed);

        add(btnStart);
        add(btnStop);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnStart) {
            chatServer.start(8189);
        }else if(src == btnStop){
//            throw new RuntimeException("Hello from EDT");
            chatServer.stop();
        }else{
            throw new RuntimeException("Unknown sourse: "+src);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        StackTraceElement[] ste = e.getStackTrace();//создаем массив элементов и достаем его из Стэк трейса
        msg = "Exeption in "+ t.getName() +" "+ e.getClass().getCanonicalName() + ": "+ e.getMessage()+" \n\t at "+ste[0];
        //JOptionPane - позволяет выкидывать окошки, отрисовываемся на основе нас(this),
        // сообщение наше,тайтл,и выбираем вид панельки которая будет выкидываться ее вид
        JOptionPane.showMessageDialog(this,msg,"Exeption",JOptionPane.ERROR_MESSAGE);
        System.exit(1);//ставим выход из системы после как пользователь нажмет на "ок" прочитав панель ошибки-
                              // программа свернется по (1)- то есть екстренно, при нормальном (0)
    }
}
