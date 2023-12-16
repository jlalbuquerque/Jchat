package com.jlalbuquerq.client;

import java.net.Socket;

public class Member {
    public Member member;
    public Socket socket;

    public Member(Socket socket, Member member) {
        this.socket = socket;
        this.member = member;
    }
}
