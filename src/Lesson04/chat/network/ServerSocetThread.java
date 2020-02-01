package Lesson04.chat.network;

public class ServerSocetThread extends Thread {

    private int port;

    public ServerSocetThread(String name, int port){
        super(name);
        this.port = port;
        start();
    }

    @Override
    public void run() {
        System.out.println("Server started");
        while (!isInterrupted()){
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                interrupt();
            }
            System.out.println("Server socket thread is working");
        }
        System.out.println("Server stopped");
    }
}
