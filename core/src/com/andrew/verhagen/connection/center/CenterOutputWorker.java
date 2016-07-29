package com.andrew.verhagen.connection.center;

import java.io.IOException;
import java.net.DatagramSocket;

class CenterOutputWorker extends Thread {

    private DatagramSocket socket;
    private ConnectionCenterHandler outputHandler;

    public CenterOutputWorker(ConnectionCenterHandler outputHandler, DatagramSocket socket) {
        this.outputHandler = outputHandler;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (true) {
                outputHandler.sendOutputData(socket);
                if (socket.isClosed())
                    break;
                System.out.println("Sending output");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new IOException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
