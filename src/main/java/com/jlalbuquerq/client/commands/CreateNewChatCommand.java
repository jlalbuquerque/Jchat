package com.jlalbuquerq.client.commands;

public class CreateNewChatCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Create new chat command received!");
    }
}
