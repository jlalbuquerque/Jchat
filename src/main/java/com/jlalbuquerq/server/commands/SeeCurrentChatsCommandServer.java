package com.jlalbuquerq.server.commands;

import com.jlalbuquerq.client.Member;
import com.jlalbuquerq.intercommunication.Command;
import com.jlalbuquerq.display.MainServer;
import com.jlalbuquerq.intercommunication.ObjectMail;
import com.jlalbuquerq.internal.collections.Pair;
import com.jlalbuquerq.server.Chat;
import com.jlalbuquerq.server.commands.internal.ChatConnectorServer;

import java.io.*;
import java.net.Socket;
import java.util.Vector;
import java.util.stream.Collectors;

public class SeeCurrentChatsCommandServer implements Command {
    public void execute(Socket socket, Member membersession) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream clientInput = new DataInputStream(socket.getInputStream());


        ObjectMail<Vector<Pair<Integer, String>>> chatObjectMail = new ObjectMail<>();
        Vector<Pair<Integer, String>> chatsInfo = MainServer.chats.stream().map(chat -> new Pair<>(chat.idChat, chat.chatName)).collect(Collectors.toCollection(Vector::new));
        chatObjectMail.sendObject(chatsInfo, output);

        while (true) {
            String option = clientInput.readUTF();
            if (option.equals("reload")) {
                chatsInfo = MainServer.chats.stream().map(chat -> new Pair<>(chat.idChat, chat.chatName)).collect(Collectors.toCollection(Vector::new));
                chatObjectMail.sendObject(chatsInfo, output);

            } else if (option.matches("\\d+")) {
                if (MainServer.chats.stream().anyMatch(chat -> chat.idChat == Integer.parseInt(option))) {
                    Chat chat = MainServer.chats.stream().filter(it -> it.idChat == Integer.parseInt(option)).findFirst().orElse(null);
                    if (chat == null) {
                        output.writeBoolean(false);
                        output.flush();
                        continue;
                    }
                    output.writeBoolean(true);
                    output.flush();

                    if (chat.needsPasswd) {
                        output.writeBoolean(true);
                        output.writeUTF(chat.getSalt());
                        String hashedPasswd = clientInput.readUTF();

                        boolean passwdCorrect = chat.checkPasswd(hashedPasswd);
                        output.writeBoolean(passwdCorrect);

                        if (!passwdCorrect) continue;
                    } else {
                        output.writeBoolean(false);
                    }

                    new ChatConnectorServer().execute(socket, chat);
                    break;
                }
                else {
                    output.writeBoolean(false);
                    output.flush();
                }

            } else if (option.equals("back")) {
                break;
            } else {
                output.writeBoolean(false);
                output.flush();
            }
        }
    }
}
