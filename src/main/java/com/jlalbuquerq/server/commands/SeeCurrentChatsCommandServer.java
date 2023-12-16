package com.jlalbuquerq.server.commands;

import com.jlalbuquerq.intercommunication.Command;
import com.jlalbuquerq.display.MainServer;
import com.jlalbuquerq.server.Chat;
import com.jlalbuquerq.server.commands.internal.ChatConnectorServer;

import java.io.*;
import java.net.Socket;

public class SeeCurrentChatsCommandServer implements Command {
    @Override
    public void execute(Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream clientInput = new DataInputStream(socket.getInputStream());



    }
}
