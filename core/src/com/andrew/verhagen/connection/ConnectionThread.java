package com.andrew.verhagen.connection;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionThread extends Thread {

    private Socket connectionSocket;
    private DataInputStream inputStream = null;
    public float position = 0f;

    @Override
    public void run() {
        try {
            connectionSocket = new Socket("192.168.0.4", 9000);
            inputStream = new DataInputStream(connectionSocket.getInputStream());
            while (true) {
                if (inputStream.available() > 4) {
                    position = inputStream.readFloat();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void end() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connectionSocket != null) {
                connectionSocket.close();
            }

        } catch (IOException closingException) {
        }
    }
}
