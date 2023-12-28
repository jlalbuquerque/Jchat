package com.jlalbuquerq.client.commands;

import com.jlalbuquerq.client.commands.internal.ChatConnectorClient;
import com.jlalbuquerq.intercommunication.ObjectMail;
import com.jlalbuquerq.internal.collections.Pair;
import com.jlalbuquerq.internal.security.SecurePasswd;

import java.io.*;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;

public class SeeCurrentChatsCommandClient {
    private static final Scanner input = new Scanner(System.in);

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
                output.flush();
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
                output.flush();
                boolean result = serverInput.readBoolean();
                if (result) {
                    boolean needsPasswd = serverInput.readBoolean();

                    if (needsPasswd) {
                        String salt = serverInput.readUTF();

                        System.out.println("This chat requires a password");
                        System.out.print("Type the password: ");
                        String passwd = input.nextLine().strip().toLowerCase(Locale.ROOT);

                        String hashedPasswd = SecurePasswd.hashPassword(passwd, salt);
                        output.writeUTF(hashedPasswd);

                        boolean passwdCorrect = serverInput.readBoolean();
                        if (!passwdCorrect) {
                            System.out.println("Incorrect password");
                            continue;
                        }
                    }

                    if (serverInput.readBoolean()) {
                        new ChatConnectorClient().execute(socket);
                        break;
                    } else {
                        System.out.println("You were banned from this chat");
                    }
                } else {
                    System.out.println("Chat wasn't found, try again");
                }
            }
        }
    }
}
