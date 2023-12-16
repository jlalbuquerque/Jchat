package com.jlalbuquerq.client.commands;

import com.jlalbuquerq.client.commands.internal.ChatConnectorClient;
import com.jlalbuquerq.intercommunication.Command;
import com.jlalbuquerq.intercommunication.ObjectMail;
import com.jlalbuquerq.server.Chat;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

public class SeeCurrentChatsCommandClient implements Command {
    private static final Scanner input = new Scanner(System.in);

    @Override
    public void execute(Socket socket) throws IOException, ClassNotFoundException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream serverInput = new DataInputStream(socket.getInputStream());


        ObjectMail<Vector<Chat>> vectorObjectMail = new ObjectMail<>();
        Vector<Chat> chats = vectorObjectMail.receiveObject(serverInput);

        if (chats.isEmpty()) {
            System.out.println("There is no chat right now");
        }
        else for (Chat chat : chats) {
            System.out.printf("id: %d | name: %s\n", chat.idChat, chat.chatName);
        }

        while (true) {
            System.out.print("Type your chat id (back to go back | reload to reload chats): ");
            String option = input.nextLine().strip().toLowerCase(Locale.ROOT);

            if (option.equals("back")) {
                output.writeUTF("back");
                break;
            }
            else if (option.equals("reload")) {
                output.writeUTF("reload");
                chats = vectorObjectMail.receiveObject(serverInput);
                if (chats.isEmpty()) {
                    System.out.println("There is no chat right now");
                } else for (Chat chat : chats) {
                    System.out.printf("id: %d | name: %s\n", chat.idChat, chat.chatName);
                }
            } else {
                output.writeUTF(option);
                boolean result = serverInput.readBoolean();
                if (result) {
                    input.close();
                    serverInput.close();
                    output.close();
                    new ChatConnectorClient().execute(socket);
                    break;
                }
                System.out.println("Chat wasn't found, try again");
            }
        }
    }
}
