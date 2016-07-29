package com.andrew.verhagen.connection.center;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
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
                System.out.println("Waiting for input on " + inputSocket.getLocalSocketAddress());
                inputSocket.receive(inputPacket);
                System.out.println("Received Input");
                inputData.limit(inputPacket.getLength());
                inputHandler.refreshHandlerWithInput(inputData, (InetSocketAddress) inputPacket.getSocketAddress());
                inputData.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Closing socket");
            inputSocket.close();
        }
    }
}
