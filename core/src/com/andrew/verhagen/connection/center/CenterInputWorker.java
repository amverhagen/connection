package com.andrew.verhagen.connection.center;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

class CenterInputWorker extends Thread {

    private DatagramSocket inputSocket;
    private DatagramPacket inputPacket;
    private ByteBuffer inputData;
    private ConnectionCenterHandler inputHandler;
    private ConnectionCenter connectionCenter;

    public CenterInputWorker(DatagramSocket inputSocket, ConnectionCenter connectionCenter, ConnectionCenterHandler inputHandler) {
        this.inputSocket = inputSocket;
        this.inputHandler = inputHandler;
        this.inputData = ByteBuffer.allocate(inputHandler.connectionSize);
        this.inputPacket = new DatagramPacket(inputData.array(), inputData.capacity());
        this.connectionCenter = connectionCenter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                inputSocket.receive(inputPacket);
                ConnectionAddress inputAddress = new ConnectionAddress(inputPacket.getAddress(), inputPacket.getPort(), 0);
                inputHandler.handleNewInput(inputData, inputAddress);
            }
        } catch (Exception e) {
            connectionCenter.emptyConnectionCenter();
        }
    }
}
