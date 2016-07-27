package com.andrew.verhagen.connection.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ConnectionCenter {

    protected DatagramSocket connectionSocket;

    protected InputConnection inputConnection;
    protected OutputConnection outputConnection;

    protected int timeOutTimeInMilliSeconds;
    protected final int maxConnections;
    protected ArrayList<ConnectionAddress> activeConnectionAddresses;

    public ConnectionCenter(int timeOutTimeInMilliSeconds, int maxConnections) {
        this.timeOutTimeInMilliSeconds = timeOutTimeInMilliSeconds;
        this.maxConnections = maxConnections;
    }

    public boolean addConnection(ConnectionAddress incomingConnectionAddress) {
        if (activeConnectionAddresses.size() >= maxConnections || holdingConnection(incomingConnectionAddress))
            return false;

        activeConnectionAddresses.add(incomingConnectionAddress);
        if (activeConnectionAddresses.size() == 1) {
            try {
                connectionSocket = new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean addConnection(InetAddress address, int port) {
        return addConnection(new ConnectionAddress(address, port, timeOutTimeInMilliSeconds));
    }

    public boolean holdingConnection(ConnectionAddress incomingConnectionAddress) {
        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            if (connectionAddress.equals(incomingConnectionAddress)) return true;
        }
        return false;
    }

    protected class CenterOutputConnection extends OutputWorker {

        public CenterOutputConnection() {
            super(connectionSocket);
        }

    }

    protected class CenterInputConnection extends InputConnection {

        public CenterInputConnection() {
            super(connectionSocket);
        }

        @Override
        protected boolean runCondition() {
            return false;
        }

        @Override
        protected void handleInputData() {

        }

        @Override
        protected void handleFinally() {

        }
    }
}
