package com.andrew.verhagen.connection.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public abstract class OutputWorker extends Thread {

    protected ByteBuffer outputData;
    protected DatagramPacket outputPacket;
    protected DatagramSocket outputSocket;

    public OutputWorker(DatagramSocket outputSocket) {
        this.outputSocket = outputSocket;
    }

    @Override
    public void run() {
        try {
            while (runCondition()) {
                sendOutputData();
            }
        } catch (IOException e) {

        } finally {
            handleFinally();
        }
    }

    protected abstract boolean runCondition();

    protected abstract void sendOutputData() throws IOException;

    protected abstract void handleFinally();
}
