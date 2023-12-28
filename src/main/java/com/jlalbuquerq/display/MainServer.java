package com.jlalbuquerq.display;

import com.jlalbuquerq.server.chat.Chat;
import com.jlalbuquerq.server.ConnectionMaintainer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.*;

public class MainServer {
    public static Set<String> usernames = Collections.synchronizedSet(new HashSet<>());
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    public static Vector<Chat> chats = new Vector<>();

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            throw new RuntimeException("There's no port available right now.");
        }
        System.out.println("The server port is " + serverSocket.getLocalPort());

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ConnectionMaintainer connectionMaintainer = new ConnectionMaintainer(socket);
                threadPool.execute(connectionMaintainer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
