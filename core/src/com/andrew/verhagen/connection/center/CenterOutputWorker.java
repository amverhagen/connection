package com.andrew.verhagen.connection.center;

import java.io.IOException;

class CenterOutputWorker extends Thread {

    private ConnectionCenterHandler outputHandler;

    public CenterOutputWorker(ConnectionCenterHandler outputHandler) {
        this.outputHandler = outputHandler;
    }

    @Override
    public void run() {
        try {
            while (true) {
                outputHandler.sendOutputData(socket);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new IOException();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
