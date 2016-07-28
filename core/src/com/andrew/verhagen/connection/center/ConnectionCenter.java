package com.andrew.verhagen.connection.center;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ConnectionCenter {

    protected DatagramSocket connectionSocket;

    private CenterInputWorker inputWorker;
    private CenterOutputWorker outputWorker;
    protected final int maxConnections;
    protected final int timeOutTimeInMilliSeconds;
    private ConnectionCenterHandler centerHandler;


    public ConnectionCenter(int maxConnections, int timeOutTimeInMilliSeconds, ConnectionCenterHandler centerHandler) {
        this.maxConnections = maxConnections;
        this.timeOutTimeInMilliSeconds = timeOutTimeInMilliSeconds;
        this.centerHandler = centerHandler;
        this.centerHandler.setConnectionCenter(this);
    }

    public synchronized boolean addAddress(InetAddress address, int port) {
        return centerHandler.addAddress(new ConnectionAddress(address, port, timeOutTimeInMilliSeconds));
    }

    public synchronized boolean addAddress(ConnectionAddress incomingConnectionAddress) {
        return centerHandler.addAddress(incomingConnectionAddress);
//        if (activeConnectionAddresses.size() >= maxConnections || holdingConnection(incomingConnectionAddress))
//            return false;
//
//        activeConnectionAddresses.add(incomingConnectionAddress);
//
//        if (centerIsEmpty)
//            startCenter();
//
//        return true;
    }

    protected synchronized void startCenter() {
        try {
            connectionSocket = new DatagramSocket();
            inputWorker = new CenterInputWorker(connectionSocket, this, centerHandler);
            outputWorker = new CenterOutputWorker(centerHandler, connectionSocket);
            inputWorker.start();
            outputWorker.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean holdingConnection(ConnectionAddress incomingConnectionAddress) {
        return centerHandler.holdingConnection(incomingConnectionAddress);
    }

//    synchronized void refreshConnections(InetAddress address, int port) throws Exception {
//        long receptionTime = System.nanoTime();
//        ConnectionAddress incomingAddress = null;
//        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
//            if ((receptionTime - connectionAddress.timeOfLastInput) >= connectionAddress.timeOutTimeInNanoSeconds) {
//                expiredConnectionAddresses.add(connectionAddress);
//            } else if (connectionAddress.hasSameAddressAndPort(address, port)) {
//                incomingAddress = connectionAddress;
//            }
//        }
//
//        removeExpiredAddresses();
//
//        if (incomingAddress != null) {
//            inputData.limit(inputPacket.getLength());
//            handleNewInput(inputData, incomingAddress);
//            inputData.clear();
//        }
//    }
//
//    private synchronized void removeExpiredAddresses() {
//        for (ConnectionAddress expiredAddress : expiredConnectionAddresses) {
//            activeConnectionAddresses.remove(expiredAddress);
//        }
//    }

    synchronized void emptyConnectionCenter() {
        if (!connectionSocket.isClosed())
            connectionSocket.close();
    }
}
