package com.andrew.verhagen.connection.center;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public final class ConnectionCenter {

    protected DatagramSocket connectionSocket;

    private CenterInputWorker inputWorker;
    private CenterOutputWorker outputWorker;
    private ConnectionCenterHandler centerHandler;

    public ConnectionCenter(ConnectionCenterHandler centerHandler) {
        this.centerHandler = centerHandler;
        this.centerHandler.setConnectionCenter(this);
    }

    public synchronized boolean addAddress(InetSocketAddress incomingAddress) {
        if (connectionSocket == null || connectionSocket.isClosed()) {
            restartCenter();
        }
        return centerHandler.addAddress(incomingAddress);
    }

    public synchronized boolean holdingConnection(ConnectionAddress incomingConnectionAddress) {
        return centerHandler.holdingConnection(incomingConnectionAddress);
    }

    private void restartCenter() {
        try {
            connectionSocket = new DatagramSocket();
            connectionSocket.setSoTimeout(centerHandler.timeOutTimeInMilliseconds);
            centerHandler.resetHandler(connectionSocket);
            inputWorker = new CenterInputWorker(connectionSocket, centerHandler);
            outputWorker = new CenterOutputWorker(centerHandler, connectionSocket);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void startCenter() {
        inputWorker.start();
        outputWorker.start();
    }
}