package com.jlalbuquerq.client.commands;

import com.jlalbuquerq.client.commands.internal.ChatConnectorClient;
import com.jlalbuquerq.intercommunication.Command;
import com.jlalbuquerq.intercommunication.ObjectMail;
import com.jlalbuquerq.internal.collections.Pair;
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


        ObjectMail<Vector<Pair<Integer, String>>> chatObjectMail = new ObjectMail<>();
        Vector<Pair<Integer, String>> chatsInfo = chatObjectMail.receiveObject(serverInput);

        if (chatsInfo.isEmpty()) {
            System.out.println("There is no chat right now");
        } else for (Pair<Integer, String> chatPair : chatsInfo) {
            System.out.printf("id: %d | name: %s\n", chatPair.first, chatPair.second);
        }

        while (true) {
            System.out.print("Type your chat id (back to go back | reload to reload chats): ");
            String option = input.nextLine().strip().toLowerCase(Locale.ROOT);

            if (option.equals("back")) {
                output.writeUTF("back");
                break;
            } else if (option.equals("reload")) {
                output.writeUTF("reload");
                chatsInfo = chatObjectMail.receiveObject(serverInput);
                if (chatsInfo.isEmpty()) {
                    System.out.println("There is no chat right now");
                } else for (Pair<Integer, String> chatPair : chatsInfo) {
                    System.out.printf("id: %d | name: %s\n", chatPair.first, chatPair.second);
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
