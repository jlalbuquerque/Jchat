package com.jlalbuquerq.client.commands;

import com.jlalbuquerq.client.commands.internal.ChatConnectorClient;
import com.jlalbuquerq.intercommunication.Command;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SeeCurrentChatsCommandClient implements Command {
    private static final Scanner input = new Scanner(System.in);

    @Override
    public void execute(Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream serverInput = new DataInputStream(socket.getInputStream());



    }
}
