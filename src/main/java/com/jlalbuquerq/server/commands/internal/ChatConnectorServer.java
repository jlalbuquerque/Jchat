package com.jlalbuquerq.server.commands.internal;

import com.jlalbuquerq.client.Member;
import com.jlalbuquerq.server.Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatConnectorServer {
    public void execute(Socket socket, Chat chat, Member membersession) throws IOException {
        System.out.println("Running ChatConnectorServer");
        chat.clientsSockets.add(socket);

        DataInputStream clientInput = new DataInputStream(socket.getInputStream());

        try {
            while (true) {
                String line = clientInput.readUTF();
                chat.sendMessage(line, membersession);

                if (line.equals("!exit")) {
                    chat.clientsSockets.remove(socket);
                    return;
                }
            }
        } catch (Exception ignore) {
        }
    }
}
