package com.jlalbuquerq.display;

import com.jlalbuquerq.client.commands.Command;
import com.jlalbuquerq.client.commands.CreateNewChatCommand;
import com.jlalbuquerq.client.commands.SeeCurrentChatsCommand;
import com.jlalbuquerq.intercommunication.CommandCommunicationSetter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static com.jlalbuquerq.internal.ScreenMethods.cls;

public class MainMember {
    static Scanner input = new Scanner(System.in);
    static Socket socket;
    static DataOutputStream output;
    static DataInputStream dataInputServerStream;

    public static void main(String[] args) throws IOException {
        socket = setSocketConnection();

        try {  // Setting socket connection
            output = new DataOutputStream(socket.getOutputStream());
            dataInputServerStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String username;  // Setting username
        try {
            username = setUsername();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Username was set! Your username will be: " + username);

        CommandCommunicationSetter<Command> commandSetter = new CommandCommunicationSetter<>();
        while (true) {
            createOrEnterChat(commandSetter);
        }
    }

    private static void createOrEnterChat(CommandCommunicationSetter<Command> commandSetter) throws IOException {
        System.out.println("""
                What do you want to do?:
                1: Create new chat;
                2: Enter existing chat.""");

        System.out.print("Your option: ");
        String option = input.nextLine().strip();
        while (!option.equals("1") && !option.equals("2")) {
            System.out.println("Invalid option, try again (1 or 2)");
            System.out.print("Your option: ");
            option = input.nextLine().strip();
        }

        Command action;
        if (option.equals("1")) {
            action = new CreateNewChatCommand();
        } else {
            action = new SeeCurrentChatsCommand();
        }

        commandSetter.sendCommand(action, output);
    }

    private static Socket setSocketConnection() {
        System.out.println("Write the server port (there must be a open server)");
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int port;
        System.out.print("Server port: ");
        port = Integer.parseInt(input.nextLine());
        while (true) {
            try {
                Socket socket = new Socket("localhost", port);
                cls();
                return socket;
            } catch (IOException e) {
                System.out.println("Server port is invalid, try again");
                System.out.print("Server port: ");
                port = Integer.parseInt(input.nextLine());
            }
        }
    }

    private static String setUsername() throws InterruptedException, IOException {
        System.out.println("First, let's define your username");
        Thread.sleep(500);

        String username;

        while (true) {
            System.out.print("Your username is: ");
            username = input.nextLine().strip();

            if (username.isBlank()) {
                System.out.println("Your name must not be blank, try other username: ");
                continue;
            } else if (!username.matches("\\w+")) {
                System.out.println("Your name must contain only alphanumeric characters, try other username: ");
                continue;
            } else if (usernamesContains(username)) {
                System.out.println("Someone already uses this username, try another: ");
                continue;
            }

            break;
        }

        cls();
        return username;
    }

    private static boolean usernamesContains(String username) throws IOException {
        output.writeUTF(username + "\n");
        output.flush();

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        return dataInputStream.readBoolean();
    }
}
