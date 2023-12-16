package com.jlalbuquerq.server;

import com.jlalbuquerq.intercommunication.Command;
import com.jlalbuquerq.display.MainServer;
import com.jlalbuquerq.intercommunication.CommandCommunicationSetter;

import java.io.*;
import java.net.Socket;

public class ConnectionMaintainer implements Runnable {
    private final Socket socket;
    public String clientUsername;
    public DataInputStream clientInput;
    public DataOutputStream output;

    public ConnectionMaintainer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //  Input and Output setting
        try {
            clientInput = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // username reader
        usernamevalidator();
        System.out.println("Current username list: " + MainServer.usernames);


        // Command receiver
        while (true) {
            CommandCommunicationSetter commSet = new CommandCommunicationSetter();
            Command command;
            try {
                command = commSet.receiveCommand(clientInput);
                command.execute(socket);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void usernamevalidator() {
        try {
            clientUsername = clientInput.readUTF();
            output.writeBoolean(MainServer.usernames.contains(clientUsername));
            output.flush();

            boolean successful = MainServer.usernames.add(clientUsername);
            System.out.println("Tried to add new username: " + clientUsername);
            System.out.println(MainServer.usernames);

            while (!successful) {
                clientUsername = clientInput.readUTF();
                output.writeBoolean(MainServer.usernames.contains(clientUsername));

                successful = MainServer.usernames.add(clientUsername);
                System.out.println("Tried to add new username: " + clientUsername);
                System.out.println(MainServer.usernames);
            }
        } catch (IOException e) {
            System.out.println("Got an Exception");
            throw new RuntimeException(e);
        }
    }
}
