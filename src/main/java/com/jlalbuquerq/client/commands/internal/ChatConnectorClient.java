package com.jlalbuquerq.client.commands.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatConnectorClient {
    public void execute(Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream serverInput = new DataInputStream(socket.getInputStream());
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        System.out.println("Running Chat Connector Client");

        threadPool.execute(messageInput(output, threadPool));

        threadPool.execute(getMessages(serverInput));

        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Runnable getMessages(DataInputStream serverInput) {
        return () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String line = serverInput.readUTF();
                    System.out.println(line);
                }
            } catch (Exception ignore) {
            }
        };
    }

    private static Runnable messageInput(DataOutputStream output, ExecutorService threadPool) {
        return () -> {
            Scanner input = new Scanner(System.in);
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String line = input.nextLine();

                    if (line.isBlank()) continue;

                    output.writeUTF(line);
                    output.flush();

                    if (line.equals("!exit")) {
                        threadPool.shutdownNow();
                        return;
                    }
                }
            } catch (Exception ignore) {
            }
        };
    }
}
