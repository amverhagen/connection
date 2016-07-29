package com.andrew.verhagen.connection.center;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

class CenterInputWorker extends Thread {

    private DatagramSocket inputSocket;
    private DatagramPacket inputPacket;
    private ByteBuffer inputData;
    private ConnectionCenterHandler inputHandler;

    public CenterInputWorker(DatagramSocket inputSocket, ConnectionCenterHandler inputHandler) {
        this.inputSocket = inputSocket;
        this.inputHandler = inputHandler;
        this.inputData = ByteBuffer.allocate(inputHandler.maxPacketSizeInBytes);
        this.inputPacket = new DatagramPacket(inputData.array(), inputData.capacity());
    }

    @Override
    public void run() {
        try {
            while (true) {
                inputSocket.receive(inputPacket);
                ConnectionAddress inputAddress = new ConnectionAddress(inputPacket.getAddress(), inputPacket.getPort(), 0);
                inputData.limit(inputPacket.getLength());
                inputHandler.refreshHandlerWithInput(inputData, inputAddress);
                inputData.clear();
            }
        } catch (Exception e) {
        } finally {
            inputSocket.close();
        }
    }
}
