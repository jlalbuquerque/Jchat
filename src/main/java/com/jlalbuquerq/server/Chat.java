package com.jlalbuquerq.server;

import com.jlalbuquerq.client.Admin;
import com.jlalbuquerq.client.Member;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

public class Chat {
    public int idChat;
    private SecretKey key;
    private String chatName;
    private Vector<Member> members;
    private Admin admin;
    private Vector<Socket> clientSockets;

    public Chat(String chatName) throws NoSuchAlgorithmException {
        this.chatName = chatName;
        this.key = generateKey();
    }

    private SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // for example
        return keyGen.generateKey();
    }
}
