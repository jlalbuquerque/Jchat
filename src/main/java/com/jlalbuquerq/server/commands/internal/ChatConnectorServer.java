package com.jlalbuquerq.server.commands.internal;

import com.jlalbuquerq.client.Member;
import com.jlalbuquerq.server.Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatConnectorServer {
    public void execute(Socket socket, Chat chat, Member membersession) throws IOException {
        System.out.println("Running ChatConnectorServer");
        chat.clientsSockets.add(socket);

        DataInputStream clientInput = new DataInputStream(socket.getInputStream());
        ExecutorService threadPool = Executors.newSingleThreadExecutor();


        threadPool.execute(() -> {
            try {
                while (true) {
                    String line = clientInput.readUTF();
                    chat.sendMessage(line, membersession);
                }
            } catch (Exception ignore) {
            }
        });
        try {
            threadPool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
