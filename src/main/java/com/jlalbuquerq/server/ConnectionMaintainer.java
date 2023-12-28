package com.jlalbuquerq.server;

import com.jlalbuquerq.client.Member;
import com.jlalbuquerq.intercommunication.Command;
import com.jlalbuquerq.display.MainServer;
import com.jlalbuquerq.server.commands.CreateNewChatCommandServer;
import com.jlalbuquerq.server.commands.SeeCurrentChatsCommandServer;

import java.io.*;
import java.net.Socket;

public class ConnectionMaintainer implements Runnable {
    private final Socket socket;
    private String clientUsername;

    private DataOutputStream output;
    private DataInputStream clientInput;

    public ConnectionMaintainer(Socket socket) { this.socket = socket; }

    @Override
    public void run() {
        //  Input and Output setting
        try {
            output = new DataOutputStream(socket.getOutputStream());
            clientInput = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // username reader
        usernamevalidator();
        Member membersession = new Member(clientUsername);
        System.out.println("Current username list: " + MainServer.usernames);


        // Command receiver
        while (true) {
            Command command;
            try {
                int i = clientInput.readInt();
                System.out.println("Received command " + i);
                if (i == 1) {
                    command = new CreateNewChatCommandServer();
                } else {
                    command = new SeeCurrentChatsCommandServer();
                }
                command.execute(socket, membersession);
            } catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
        }
    }

    private void usernamevalidator() {
        try {
            clientUsername = clientInput.readUTF();
            output.writeBoolean(MainServer.usernames.contains(clientUsername));
            output.flush();

            boolean successful = MainServer.usernames.add(clientUsername);
            System.out.println("Tried to add new username: " + clientUsername);

            while (!successful) {
                clientUsername = clientInput.readUTF();
                output.writeBoolean(MainServer.usernames.contains(clientUsername));
                output.flush();

                successful = MainServer.usernames.add(clientUsername);
                System.out.println("Tried to add new username: " + clientUsername);
                System.out.println(MainServer.usernames);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
