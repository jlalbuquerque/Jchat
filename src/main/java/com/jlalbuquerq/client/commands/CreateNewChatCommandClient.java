package com.jlalbuquerq.client.commands;

import com.jlalbuquerq.intercommunication.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CreateNewChatCommandClient implements Command {
    private final static Scanner input = new Scanner(System.in);

    @Override
    public void execute(Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream serverInput = new DataInputStream(socket.getInputStream());

        String chatName;
        while (true) {
            System.out.print("Type the chat name: ");
            chatName = input.nextLine();
            if (chatName.isBlank()) {
                System.out.println("Your chat name must not be blank");
                continue;
            }
            break;
        }
    }
}
