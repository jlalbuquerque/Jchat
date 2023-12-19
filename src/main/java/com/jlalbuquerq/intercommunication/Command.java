package com.jlalbuquerq.intercommunication;

import com.jlalbuquerq.client.Member;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public interface Command extends Serializable {
    void execute(Socket socket, Member membersession) throws IOException, ClassNotFoundException;
}
