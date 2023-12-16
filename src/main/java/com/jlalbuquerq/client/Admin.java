package com.jlalbuquerq.client;

import java.net.Socket;

public class Admin extends Member {
    public Admin(Socket socket, Member member) {
        super(socket, member);
    }
}
