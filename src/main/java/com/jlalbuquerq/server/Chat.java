package com.jlalbuquerq.server;

import com.jlalbuquerq.client.Member;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Chat {
    private static final AtomicInteger numberChat = new AtomicInteger(0);

    public int idChat;
    public String chatName;

    public boolean needsPasswd;
    private String hashPasswd;
    private String salt;

    private final Member admin;
    public final Vector<Socket> clientsSockets;

    // Public chat
    public Chat(String chatName, Member admin, String passwd, String salt) {
        this.chatName = chatName;
        this.idChat = numberChat.addAndGet(1);
        this.admin = admin;
        this.clientsSockets = new Vector<>();
        this.needsPasswd = true;
        this.hashPasswd = passwd;
        this.salt = salt;
    }

    // Private chat
    public Chat(String chatName, Member adm) {
        this.chatName = chatName;
        this.idChat = numberChat.addAndGet(1);
        this.admin = adm;
        this.clientsSockets = new Vector<>();
        this.needsPasswd = false;
    }

    public void sendMessage(String message, Member member) {
        for (Socket socket : clientsSockets) {
            try {
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF("%s: %s".formatted(member.username, message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean checkPasswd(String hashedPasswdInput) {
        return this.hashPasswd.equals(hashedPasswdInput);
    }

    public String getSalt() {
        return salt;
    }
}