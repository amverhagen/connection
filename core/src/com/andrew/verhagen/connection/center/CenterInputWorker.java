package com.andrew.verhagen.connection.center;

import java.net.DatagramPacket;

class CenterInputWorker extends Thread {

    private DatagramPacket inputPacket;
    private ConnectionCenterHandler inputHandler;

    public CenterInputWorker(ConnectionCenterHandler inputHandler) {
        this.inputHandler = inputHandler;
        this.inputPacket = new DatagramPacket(new byte[inputHandler.maxPacketSizeInBytes], inputHandler.maxPacketSizeInBytes);
    }

    @Override
    public void run() {
        try {
            while (true) {
                inputHandler.getSocket().receive(inputPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Closing socket");
            inputSocket.close();
        }
    }
}
