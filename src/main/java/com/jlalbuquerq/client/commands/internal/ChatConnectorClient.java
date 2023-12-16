package com.jlalbuquerq.client.commands.internal;

import com.jlalbuquerq.intercommunication.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatConnectorClient {
    public void execute(Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream serverInput = new DataInputStream(socket.getInputStream());


        System.out.println("Running Chat Connector Client");
    }
}
