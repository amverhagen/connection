package com.andrew.verhagen.connection.center;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public final class ConnectionCenter {

    private boolean isCenterOn;
    private CenterInputWorker inputWorker;
    private CenterOutputWorker outputWorker;
    private ConnectionCenterHandler centerHandler;

    private DatagramSocket connectionSocket;

    public ConnectionCenter(ConnectionCenterHandler centerHandler) {
        this.centerHandler = centerHandler;
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

    public synchronized void closeCenter() {
        if (connectionSocket != null) {
            connectionSocket.close();
        }
        this.isCenterOn = false;
    }

    public synchronized boolean startCenter(ConnectionCenterHandler handler) {
        if (this.isActive())
            return false;
        this.centerHandler = handler;
        this.connectionSocket = handler.getSocket();
        inputWorker = new CenterInputWorker(centerHandler);
        outputWorker = new CenterOutputWorker(centerHandler);
        return true;
    }

    public synchronized void closeConnectionCenter() {
        if (connectionSocket != null)
            connectionSocket.close();
        this.centerHandler = null;
        this.inputWorker = null;
        this.outputWorker = null;
    }

    protected synchronized boolean interruptHandlerForOutput() {
        return false;
    }

    protected synchronized boolean interruptHandlerWithInput(ByteBuffer inputBuffer, InetSocketAddress remoteAddress) {
        return false;
    }

    private boolean isActive() {
        return connectionSocket != null && !connectionSocket.isClosed();
    }
}