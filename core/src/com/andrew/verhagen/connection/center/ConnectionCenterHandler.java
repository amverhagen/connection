package com.andrew.verhagen.connection.center;

import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class ConnectionCenterHandler {

    protected ConnectionCenter connectionCenter;
    protected int maxConnections;
    final int connectionSize;
    protected ArrayList<ConnectionAddress> activeConnectionAddresses;
    protected ArrayList<ConnectionAddress> expiredConnectionAddresses;

    public ConnectionCenterHandler(int maxPacketSizeInBytes) {
        connectionSize = maxPacketSizeInBytes;
    }

    void setConnectionCenter(ConnectionCenter connectionCenter) {
        this.connectionCenter = connectionCenter;
    }

    public abstract boolean addAddress(ConnectionAddress incomingAddress);

    //create output data,
    //foreach conn, send on socket
    public abstract void sendOutputData(DatagramSocket outputSocket);

    public abstract boolean holdingConnection(ConnectionAddress incomingAddress);

    protected abstract void handleNewInput(ByteBuffer inputData, ConnectionAddress inputAddress) throws Exception;
}
