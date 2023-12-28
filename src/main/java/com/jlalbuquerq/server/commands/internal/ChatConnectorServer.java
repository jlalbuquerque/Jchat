package com.jlalbuquerq.server.commands.internal;

import com.jlalbuquerq.client.Member;
import com.jlalbuquerq.server.chat.Chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatConnectorServer {
    private final Socket socket;
    private final Member membersession;
    private final Chat chat;
    private final AtomicBoolean ended = new AtomicBoolean(false);

    private DataInputStream clientInput;

    public ChatConnectorServer(Socket socket, Member membersession, Chat chat) {
        this.socket = socket;
        this.membersession = membersession;
        this.chat = chat;
    }

    private Thread t;

    public void execute() throws IOException {
        chat.addMember(membersession, socket, this);

        clientInput = new DataInputStream(socket.getInputStream());

        t = new Thread(() -> {
            try {
                while (!ended.get()) {
                    if (clientInput.available() > 0) {
                        String line = clientInput.readUTF();
                        if (ended.get()) return;

                        chat.sendMessage(line, membersession);

                        if (line.equals("!exit")) {
                            chat.memberSocketMap.remove(membersession);
                            chat.memberConnectorMap.remove(membersession);
                            return;
                        }
                    }
                }
            } catch (Exception ignore) {}
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() throws IOException {
        ended.set(true);
        t.interrupt();
    }
}