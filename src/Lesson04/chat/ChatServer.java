package Lesson04.chat;

import Lesson04.chat.network.ServerSocetThread;

public class ChatServer {

    public ServerSocetThread server;

    public void start(int port) {
        if (server != null && server.isAlive())
            System.out.println("Server already started");
        else
            server = new ServerSocetThread("Server", port);
    }

    public void stop() {
        if (server == null || !server.isAlive()) {
            System.out.println("Server is not running");
        } else {
            server.interrupt();
        }
    }
}