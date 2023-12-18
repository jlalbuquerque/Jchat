package com.jlalbuquerq.server.commands;

import com.jlalbuquerq.intercommunication.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CreateNewChatCommandServer implements Command {
    @Override
    public void execute(Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream clientInput = new DataInputStream(socket.getInputStream());


        System.out.println("Create new chat command received!");
    }
}
