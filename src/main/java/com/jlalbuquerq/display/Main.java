package com.jlalbuquerq.display;

import java.util.Locale;
import java.util.Scanner;

import static com.jlalbuquerq.internal.ScreenMethods.cls;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello, welcome to Jchat! There you can communicate yourself with many other people in real time");
        Thread.sleep(1500);

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