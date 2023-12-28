package com.jlalbuquerq.client.commands.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChatConnectorClient {
    private DataOutputStream output;
    private DataInputStream serverInput;
    private ExecutorService threadPool = Executors.newFixedThreadPool(2);
    private final AtomicBoolean kicked = new AtomicBoolean(false);
    private Scanner input;

    public void execute(Socket socket) throws IOException {
        output = new DataOutputStream(socket.getOutputStream());
        serverInput = new DataInputStream(socket.getInputStream());

        threadPool.execute(this::messageInput);
        threadPool.execute(this::getMessages);

        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMessages() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String line = serverInput.readUTF();

                if (line.equals("FORCE_LEAVE")) {
                    kicked.set(true);
                    threadPool.shutdownNow();
                    System.out.println("You were kicked from this chat (press ENTER to go back to menu)");
                    break;
                }

                System.out.println(line);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void messageInput() {
        input = new Scanner(System.in);
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String line = input.nextLine();

                if (line.isBlank()) continue;

                if (this.kicked.get()) return;
                else {
                    output.writeUTF(line);
                    output.flush();

                    if (line.equals("!exit")) {
                        kicked.set(true);
                        threadPool.shutdownNow();
                        threadPool.shutdown();
                        return;
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }
}
