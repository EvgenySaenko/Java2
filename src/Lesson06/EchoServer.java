package Lesson06;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8189);
            Socket currentClient = serverSocket.accept()){  //кладем в трай с ресурсами сервер сокет и сокет(полусокет)
            System.out.println("к нам подключился клиент!");//и когда они прекращают взаимодействие трай автоматически закрывает их потоки
            //дальше  мы можем с ним взаимодействовать
            DataInputStream in = new DataInputStream(currentClient.getInputStream());
            DataOutputStream out = new DataOutputStream(currentClient.getOutputStream());
            String b = in.readUTF();
            out.writeUTF("Echo: "+ b);

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
