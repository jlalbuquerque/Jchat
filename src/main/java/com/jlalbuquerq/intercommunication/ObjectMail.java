package com.jlalbuquerq.intercommunication;

import java.io.*;

public class ObjectMail<T> {

    public void sendObject(T obj, DataOutputStream output) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();

        byte[] bytes = baos.toByteArray();
        output.writeInt(bytes.length);
        output.flush();
        output.write(bytes);
        output.flush();
    }

    public T receiveObject(DataInputStream input) throws IOException, ClassNotFoundException {
        int length = input.readInt();
        if (length > 0) {
            byte[] bytes = new byte[length];
            input.readFully(bytes, 0, bytes.length);

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        }
        return null;
    }
}
