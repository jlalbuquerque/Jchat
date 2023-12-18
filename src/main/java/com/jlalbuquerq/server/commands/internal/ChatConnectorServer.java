package com.jlalbuquerq.server.commands.internal;

import com.jlalbuquerq.server.Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatConnectorServer {
    public void execute(Socket socket, Chat chat) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream clientInput = new DataInputStream(socket.getInputStream());



    }
}
