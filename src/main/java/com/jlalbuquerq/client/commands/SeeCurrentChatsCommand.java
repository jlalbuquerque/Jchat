package com.jlalbuquerq.client.commands;

public class SeeCurrentChatsCommand implements Command {
    @Override
    public void execute() {
        System.out.println("See current chats command received!");
    }
}
