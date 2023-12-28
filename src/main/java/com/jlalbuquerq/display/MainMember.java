package com.jlalbuquerq.display;

import com.jlalbuquerq.client.commands.CreateNewChatCommandClient;
import com.jlalbuquerq.client.commands.SeeCurrentChatsCommandClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static com.jlalbuquerq.internal.display.ScreenMethods.cls;

public class MainMember {
    private static Socket socket;
    private static final Scanner input = new Scanner(System.in);
    private static DataOutputStream output;
    private static DataInputStream serverInput;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        socket = setSocketConnection();

        try {
            output = new DataOutputStream(socket.getOutputStream());
            serverInput = new DataInputStream(socket.getInputStream());
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


        // Menu
        while (true) {
            createOrEnterChat();
        }
    }

    private static void createOrEnterChat() throws IOException, ClassNotFoundException {
        System.out.println(
                """
                        What do you want to do?:
                        1: Create new chat;
                        2: Enter existing chat;
                        3: Exit."""
        );  // TODO: REFACTOR THE CODE TO THE JLINE LIBRARY

        System.out.print("Your option: ");
        String option = input.nextLine().strip();
        while (!option.equals("1") && !option.equals("2") && !option.equals("3")) {
            System.out.println("Invalid option, try again (1 or 2 or 3)");
            System.out.print("Your option: ");
            option = input.nextLine().strip();
        }

        if (option.equals("1")) {
            output.writeInt(1);
            new CreateNewChatCommandClient().execute(socket);
        } else if (option.equals("2")){
            output.writeInt(2);
            new SeeCurrentChatsCommandClient().execute(socket);
        } else {
            output.writeInt(3);
            socket.close();
            System.exit(0);
        }
    }

    private static Socket setSocketConnection() {
        System.out.println("Write the server port (there must be a open server)");

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
                port = Integer.parseInt(input.nextLine().strip());
            }
        }
    }

    private static String setUsername() throws InterruptedException, IOException {
        System.out.println("First, let's define your username");

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
        output.writeUTF(username);
        output.flush();

        return serverInput.readBoolean();
    }
}
