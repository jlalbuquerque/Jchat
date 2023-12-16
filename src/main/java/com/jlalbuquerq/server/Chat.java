package com.jlalbuquerq.server;

import com.jlalbuquerq.client.Admin;
import com.jlalbuquerq.client.Member;

import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

public class Chat {
    static int numberChat = 1;

    public int idChat;
    private SecretKey key;
    public String chatName;
    private Vector<Member> members;
    private Admin admin;
    private Vector<Socket> clientsSockets;

    public Chat(String chatName, Admin adm) throws NoSuchAlgorithmException {
        this.chatName = chatName;
        this.key = generateKey();
        this.idChat = numberChat;
        numberChat++;
        this.members = new Vector<>();
        this.admin = adm;
        this.clientsSockets = new Vector<>();
    }

    private SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

    public boolean checkKey(SecretKey inputKey) {
        return this.key.equals(inputKey);
    }
}