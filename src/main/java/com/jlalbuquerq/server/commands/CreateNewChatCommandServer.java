package com.jlalbuquerq.server.commands;

import com.jlalbuquerq.client.Member;
import com.jlalbuquerq.display.MainServer;
import com.jlalbuquerq.intercommunication.Command;
import com.jlalbuquerq.internal.security.SecurePasswd;
import com.jlalbuquerq.server.chat.Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CreateNewChatCommandServer implements Command {
    @Override
    public void execute(Socket socket, Member membersession) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream clientInput = new DataInputStream(socket.getInputStream());

        System.out.println("Create new chat command received!");

        boolean hasPasswd = clientInput.readBoolean();

        String salt = null;
        String encryptedPasswd = null;
        if (hasPasswd) {
            salt = SecurePasswd.generateSalt();
            output.writeUTF(salt);

            encryptedPasswd = clientInput.readUTF();
        }
        String chatName = clientInput.readUTF();

        Chat chat;
        if (hasPasswd)
            chat = new Chat(chatName, membersession, encryptedPasswd, salt);
        else
            chat = new Chat(chatName, membersession);

        MainServer.chats.add(chat);
    }
}
