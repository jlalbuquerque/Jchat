package com.jlalbuquerq.client.commands;

import com.jlalbuquerq.internal.security.SecurePasswd;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class CreateNewChatCommandClient {
    private final static Scanner input = new Scanner(System.in);

    public void execute(Socket socket) throws IOException {
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream serverInput = new DataInputStream(socket.getInputStream());

        String chatName;
        while (true) {
            System.out.print("Type the chat name: ");
            chatName = input.nextLine();
            if (chatName.isBlank()) {
                System.out.println("Your chat name must not be blank");
                continue;
            }
            break;
        }

        String hasPasswd;
        while (true) {
            System.out.print("Do you want to set a password to enter your chat? (Y/N): ");
            hasPasswd = input.nextLine().strip().toLowerCase();
            if (!hasPasswd.equals("y") && !hasPasswd.equals("n")) {
                System.out.println("Your option must be Y or N");
                continue;
            }
            break;
        }

        char[] passwordChars = null;
        if (hasPasswd.equals("y")) {
            while (true) {
                System.out.print("Type your chat password: ");
                passwordChars = input.nextLine().toCharArray();

                if (passwordChars.length == 0) {
                    System.out.println("Your password is blank, try again");
                    continue;
                }
                break;
            }
        }

        String salt;
        if (hasPasswd.equals("y")) {
            output.writeBoolean(true);

            salt = serverInput.readUTF();
            String encryptedPasswd = SecurePasswd.hashPassword(new String(passwordChars), salt);
            Arrays.fill(passwordChars, '0');

            output.writeUTF(encryptedPasswd);
        } else {
            output.writeBoolean(false);
        }

        output.writeUTF(chatName);
        System.out.println("Created new chat successfully, now you can enter it by the menu");
    }
}
