package com.jlalbuquerq.server.commands;

import com.jlalbuquerq.intercommunication.Command;
import com.jlalbuquerq.display.MainServer;
import com.jlalbuquerq.intercommunication.ObjectMail;
import com.jlalbuquerq.server.Chat;
import com.jlalbuquerq.server.commands.internal.ChatConnectorServer;

import java.io.*;
import java.net.Socket;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

public class SeeCurrentChatsCommandServer implements Command {
    @Override
    public void execute(Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream clientInput = new DataInputStream(socket.getInputStream());


        ObjectMail<Vector<Chat>> objectMail = new ObjectMail<>();
        objectMail.sendObject(MainServer.chats, output);

        while (true) {
            String option = clientInput.readUTF();
            if (option.equals("reload")) {
                execute(socket);
                break;
            } else if (option.matches("\\d+")) {
                if (MainServer.chats.stream().anyMatch(chat -> chat.idChat == Integer.parseInt(option))) {
                    Chat chat = MainServer.chats.stream().filter(it -> it.idChat == Integer.parseInt(option)).findFirst().orElse(null);
                    if (chat == null) {
                        output.writeBoolean(false);
                        continue;
                    }
                    output.writeBoolean(true);
                    new ChatConnectorServer().execute(socket, chat);
                    break;
                }
                else {
                    output.writeBoolean(false);
                }
            } else if (option.equals("back")) {
                break;
            } else {
                output.writeBoolean(false);
            }
        }
    }
}
