package com.andrew.verhagen.connection.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public abstract class InputConnection extends Thread {

    protected ByteBuffer inputData;
    protected DatagramPacket inputPacket;
    protected DatagramSocket inputSocket;
    protected final long timeOutTimeInNanoSeconds = 2000000000l;
    protected final int timeOutTimeInMilliSeconds = (int) (timeOutTimeInNanoSeconds / 1000000L);

    public InputConnection(DatagramSocket inputSocket) {
        this.inputSocket = inputSocket;
        this.inputData = ByteBuffer.allocate(256);
        this.inputPacket = new DatagramPacket(inputData.array(), inputData.capacity());
    }

    @Override
    public void run() {
        try {
            inputSocket.setSoTimeout(timeOutTimeInMilliSeconds);
            while (runCondition()) {
                inputSocket.receive(inputPacket);
                inputData.limit(inputPacket.getLength());
                handleInputData();
                inputData.clear();
            }
        } catch (IOException e) {

        } finally {
            handleFinally();
        }
    }

    protected abstract boolean runCondition();

    protected abstract void handleInputData();

    protected abstract void handleFinally();
}