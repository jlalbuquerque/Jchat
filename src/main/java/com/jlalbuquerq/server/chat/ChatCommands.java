package com.jlalbuquerq.server.chat;

import com.jlalbuquerq.client.Member;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;

public class ChatCommands {
    static final Member serversession = new Member("**SERVER MESSAGE**");
    private final Chat chat;

    public ChatCommands(Chat chat) {
        this.chat = chat;
    }


    private static final Set<String> commands = Set.of(
            "!ban",
            "!unban"
    );

    public void interpretMessage(String message, Member creator) {
        System.out.println("Interpreting message...");
        String[] parsedMessages = message.split(" ");

        String command = parsedMessages[0].toLowerCase(Locale.ROOT);
        if (!commands.contains(command)) {
            return;
        }
        if (command.equals("!ban")) {
            if (parsedMessages.length < 2) {
                chat.sendMessage("There is not enough arguments", serversession);
            } else {
                banMember(creator, new Member(parsedMessages[1]));
            }
        } else if (command.equals("!unban")) {
            if (parsedMessages.length < 2) {
                chat.sendMessage("There is not enough arguments", serversession);
            } else {
                unbanMember(creator, new Member(parsedMessages[1]));
            }
        }
    }

    public void banMember(Member creator, Member target) {
        System.out.println("Trying to ban " + target.username);
        if (chat.admin.equals(creator)) {  // User is allowed to ban
            System.out.println("Ban request is from the admin");
            if (target.equals(chat.admin)) {
                chat.sendMessage("The admin is not bannable", serversession);
            } else if (!chat.banList.contains(target)) {
                System.out.println("Permission conceded to ban");

                chat.banList.add(target);
                System.out.println("Added to ban list");

                System.out.println("Forcing leave");
                forceLeave(target);
                System.out.println("Ended forceLeave");


                chat.memberSocketMap.remove(target);
                chat.memberConnectorMap.remove(target);
                System.out.println("Removed member info from chat");

                chat.sendMessage("User \"" + target + "\" is now banned", serversession);
            } else {
                chat.sendMessage("The user " + target.username + " is already banned", serversession);
            }
        } else {
            chat.sendMessage("You do not have the permission to ban", serversession);
        }
    }

    private void forceLeave(Member target) {
        DataOutputStream output;
        try {
            output = new DataOutputStream(chat.memberSocketMap.get(target).getOutputStream());
            System.out.println("Got DataOutputStream of " + target.username);

            output.writeUTF("FORCE_LEAVE");
            output.flush();

            chat.memberConnectorMap.get(target).stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void unbanMember(Member creator, Member target) {
        if (chat.admin.equals(creator)) {
            boolean unBanned = chat.banList.remove(target);
            chat.sendMessage(unBanned ? "The user %s is now unbanned".formatted(target.username) : "This user is not banned", serversession);
        } else {
            chat.sendMessage("Only the admin can unban someone", serversession);
        }
    }
}
