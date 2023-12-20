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

        threadPool.execute(() -> {
            Scanner input = new Scanner(System.in);
            try {
                while (true) {
                    String line = input.nextLine();

                    if (line.isBlank()) continue;

                    output.writeUTF(line);
                    output.flush();
                }
            } catch (Exception ignore) {
            }
        });

        threadPool.execute(() -> {
            try {
                while (true) {
                    String line = serverInput.readUTF();
                    System.out.println(line);
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
