package com.jlalbuquerq.server.chat;

import com.jlalbuquerq.client.Member;
import com.jlalbuquerq.server.commands.internal.ChatConnectorServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jlalbuquerq.server.chat.ChatCommands.serversession;

public class Chat {
    private static final AtomicInteger numberChat = new AtomicInteger(0);

    public int idChat;
    public String chatName;
    public Set<Member> banList = Collections.synchronizedSet(new HashSet<>());

    public boolean needsPasswd;
    private String hashPasswd;
    private String salt;

    final Member admin;
    public final ConcurrentHashMap<Member, Socket> memberSocketMap;
    public final ConcurrentHashMap<Member, ChatConnectorServer> memberConnectorMap;
    private final ChatCommands commandInterpreter;

    // Public chat
    public Chat(String chatName, Member admin, String passwd, String salt) {
        this.chatName = chatName;
        this.idChat = numberChat.addAndGet(1);
        this.admin = admin;
        this.memberSocketMap = new ConcurrentHashMap<>();
        this.needsPasswd = true;
        this.hashPasswd = passwd;
        this.salt = salt;
        this.commandInterpreter = new ChatCommands(this);
        this.memberConnectorMap = new ConcurrentHashMap<>();
    }

    // Private chat
    public Chat(String chatName, Member adm) {
        this.chatName = chatName;
        this.idChat = numberChat.addAndGet(1);
        this.admin = adm;
        this.memberSocketMap = new ConcurrentHashMap<>();
        this.needsPasswd = false;
        this.commandInterpreter = new ChatCommands(this);
        this.memberConnectorMap = new ConcurrentHashMap<>();
    }

    public void sendMessage(String message, Member member) {
        for (Socket socket : memberSocketMap.values()) {
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF("%s: %s".formatted(member.username, message));
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        commandInterpreter.interpretMessage(message, member);
    }

    public boolean checkPasswd(String hashedPasswdInput) { return this.hashPasswd.equals(hashedPasswdInput); }

    public String getSalt() { return salt; }

    public void addMember(Member membersession, Socket socket, ChatConnectorServer chatConnectorServer) {
        memberSocketMap.put(membersession, socket);
        memberConnectorMap.put(membersession, chatConnectorServer);
        sendMessage("New member entered the chat: " + membersession, serversession);
    }
}