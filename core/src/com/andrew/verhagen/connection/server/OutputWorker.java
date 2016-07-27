package com.andrew.verhagen.connection.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class OutputWorker extends Thread {

    protected ByteBuffer outputData;
    protected DatagramPacket outputPacket;
    protected DatagramSocket outputSocket;

    public OutputWorker(DatagramSocket outputSocket) {
        this.outputSocket = outputSocket;
    }
}
