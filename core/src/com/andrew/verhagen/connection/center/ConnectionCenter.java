package com.andrew.verhagen.connection.center;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public final class ConnectionCenter {

    protected DatagramSocket connectionSocket;

    private boolean isCenterOn;
    private CenterInputWorker inputWorker;
    private CenterOutputWorker outputWorker;
    private ConnectionCenterHandler centerHandler;


    public ConnectionCenter(ConnectionCenterHandler centerHandler) {
        this.centerHandler = centerHandler;
        this.centerHandler.setConnectionCenter(this);
    }

    public ConnectionCenter(ConnectionCenterHandler centerHandler, DatagramSocket socket, InetSocketAddress address) {
        this(centerHandler);
        this.restartCenter(socket);
        this.addAddress(address);
    }

    public synchronized boolean addAddress(InetSocketAddress incomingAddress) {
        if (connectionSocket == null || connectionSocket.isClosed()) {
            restartCenter();
        }
        if (centerHandler.addAddress(incomingAddress)) {
            if (!isCenterOn)
                this.startCenter();
            return true;
        }
        return false;
    }

    public synchronized boolean holdingConnection(InetSocketAddress incomingConnectionAddress) {
        if (connectionSocket == null || connectionSocket.isClosed()) {
            restartCenter();
        }
        return centerHandler.holdingConnection(incomingConnectionAddress);
    }

    private void restartCenter() {
        try {
            restartCenter(new DatagramSocket());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void restartCenter(DatagramSocket socket) {
        try {
            System.out.println("Restarted Center");
            closeCenter();
            connectionSocket = socket;
            connectionSocket.setSoTimeout(centerHandler.timeOutTimeInMilliseconds);
            centerHandler.resetHandler(connectionSocket);
            inputWorker = new CenterInputWorker(connectionSocket, centerHandler);
            outputWorker = new CenterOutputWorker(centerHandler, connectionSocket);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void startCenter() {
        isCenterOn = true;
        inputWorker.start();
        outputWorker.start();
    }

    public void closeCenter() {
        if (connectionSocket != null) {
            connectionSocket.close();
        }
        this.isCenterOn = false;
    }
}