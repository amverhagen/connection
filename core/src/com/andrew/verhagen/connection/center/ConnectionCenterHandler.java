package com.andrew.verhagen.connection.center;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class ConnectionCenterHandler {

    protected ConnectionCenter connectionCenter;
    protected DatagramSocket socket;

    protected final int maxConnections;
    protected final int maxPacketSizeInBytes;
    protected final int timeOutTimeInMilliseconds;

    protected final DatagramPacket outputPacket;
    protected final ByteBuffer outputData;

    protected ArrayList<ConnectionAddress> activeConnectionAddresses;
    protected ArrayList<ConnectionAddress> expiredConnectionAddresses;

    public ConnectionCenterHandler(int maxPacketSizeInBytes, int maxConnections, int timeOutTimeInMilliseconds) {
        this.maxPacketSizeInBytes = maxPacketSizeInBytes;
        this.maxConnections = maxConnections;
        this.timeOutTimeInMilliseconds = timeOutTimeInMilliseconds;
        this.outputData = ByteBuffer.allocate(maxPacketSizeInBytes);
        this.outputPacket = new DatagramPacket(this.outputData.array(), this.outputData.capacity());
        this.activeConnectionAddresses = new ArrayList<ConnectionAddress>();
        this.expiredConnectionAddresses = new ArrayList<ConnectionAddress>();
    }

    protected synchronized void resetHandler(DatagramSocket socket) throws SocketException {
        this.socket = socket;
        this.activeConnectionAddresses.clear();
        this.expiredConnectionAddresses.clear();
    }

    protected synchronized final void setConnectionCenter(ConnectionCenter connectionCenter) {
        this.connectionCenter = connectionCenter;
    }

    protected synchronized final boolean holdingConnection(InetSocketAddress incomingAddress) {
        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            if (connectionAddress.hasSameAddress(incomingAddress)) return true;
        }
        return false;
    }

    synchronized final void refreshHandlerWithInput(ByteBuffer inputData, InetSocketAddress inputAddress) {
        long receptionTime = System.nanoTime();

        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            if ((receptionTime - connectionAddress.timeOfLastInput) >= connectionAddress.timeOutTimeInNanoSeconds) {
                expiredConnectionAddresses.add(connectionAddress);
            }
        }
        if (expiredConnectionAddresses.size() > 0)
            removeExpiredAddresses();

        if (this.holdingConnection(inputAddress))
            handleNewInput(inputData, inputAddress);
    }

    public abstract boolean addAddress(InetSocketAddress incomingAddress);

    protected abstract void removeExpiredAddresses();

    protected abstract void sendOutputData(DatagramSocket outputSocket) throws Exception;

    protected abstract void handleNewInput(ByteBuffer inputData, InetSocketAddress inputAddress);
}
