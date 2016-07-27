package com.andrew.verhagen.connection.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class OutputConnection extends Thread {
    protected ByteBuffer outputBuffer;
    protected DatagramPacket outputPacket;
    protected DatagramSocket socket;

    public OutputConnection() {
        outputBuffer = ByteBuffer.allocate(256);
        outputPacket = new DatagramPacket(outputBuffer.array(), outputBuffer.capacity());
    }

    public OutputConnection(DatagramSocket socket) {
        this();
        this.socket = socket;
    }
}
