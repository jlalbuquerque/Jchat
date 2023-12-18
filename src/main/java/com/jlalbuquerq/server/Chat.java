package com.jlalbuquerq.server;

import com.jlalbuquerq.client.Admin;
import com.jlalbuquerq.client.Member;

import javax.crypto.SecretKey;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Chat {
    private static final AtomicInteger numberChat = new AtomicInteger(0);

    public int idChat;
    private SecretKey key;
    public String chatName;
    private Vector<Member> members;
    private Admin admin;
    private Vector<Socket> clientsSockets;

    public Chat(String chatName, Admin adm, SecretKey key) {
        this.chatName = chatName;
        this.key = key;
        this.idChat = numberChat.addAndGet(1);
        this.members = new Vector<>();
        this.admin = adm;
        this.clientsSockets = new Vector<>();
    }

    public void sendMessage(String message) {

    }

    public boolean checkKey(SecretKey inputKey) {
        return this.key.equals(inputKey);
    }
}