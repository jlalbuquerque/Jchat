package com.jlalbuquerq.display;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import static com.jlalbuquerq.internal.display.ScreenMethods.cls;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Hello, welcome to Jchat! There you can communicate yourself with many other people in real time");

        Scanner input = new Scanner(System.in);

        System.out.print("Is this window running as the main server or as a member? (s -> server; m -> member): ");
        String serverorwindow = input.nextLine().toLowerCase(Locale.ROOT);

        while (!serverorwindow.equals("s") && !serverorwindow.equals("m")) {
            System.out.println("The only options are s (server), and m (member)");
            System.out.print("Select the option you want: ");
            serverorwindow = input.nextLine().toLowerCase(Locale.ROOT);
        }
        cls();
        if (serverorwindow.equals("s")) {
            MainServer.main(null);
        } else {
            MainMember.main(null);
        }
    }
}