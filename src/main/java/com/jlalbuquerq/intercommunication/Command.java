package com.jlalbuquerq.intercommunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public interface Command extends Serializable {
    void execute(Socket socket) throws IOException;
}
