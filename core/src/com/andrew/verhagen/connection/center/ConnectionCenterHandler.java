package com.andrew.verhagen.connection.center;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public abstract class ConnectionCenterHandler implements UDPInputHandler, OutputSender {

    protected DatagramSocket socket;

    private UDPInputHandler inputHandler;
    private OutputSender outputSender;
    private ConnectionMaintainer connectionMaintainer;

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

    public ConnectionCenterHandler(ConnectionMaintainer connectionMaintainer) {
        this.connectionMaintainer = connectionMaintainer;
        this.maxConnections = connectionMaintainer.maxConnections;
        this.maxPacketSizeInBytes = 256;
        this.timeOutTimeInMilliseconds = connectionMaintainer.timeOutTimeInMilliseconds;
        this.outputData = ByteBuffer.allocate(maxPacketSizeInBytes);
        this.outputPacket = new DatagramPacket(this.outputData.array(), this.outputData.capacity());
    }

    protected synchronized final boolean holdingConnection(InetSocketAddress incomingAddress) {
        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            if (connectionAddress.hasSameAddress(incomingAddress)) return true;
        }
        return false;
    }

    protected synchronized final boolean holdingInetSocketAddress(InetSocketAddress address) {
        return this.connectionMaintainer.holdingConnection(address);
    }

    synchronized final void refreshHandlerWithInput(ByteBuffer inputData, InetSocketAddress inputAddress) {
        long receptionTime = System.nanoTime();

        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            if ((receptionTime - connectionAddress.timeOfLastValidInput) >= connectionAddress.timeOutTimeInNanoSeconds) {
                System.err.println("Address had a timeout time of " + connectionAddress.timeOutTimeInNanoSeconds);
                System.err.println("Address last input was " + (receptionTime - connectionAddress.timeOfLastValidInput) + " nano seconds ago.");
                expiredConnectionAddresses.add(connectionAddress);
            }
        }
        if (expiredConnectionAddresses.size() > 0)
            removeExpiredAddresses();

        if (this.holdingConnection(inputAddress))
            handleNewInput(inputData, inputAddress);
    }

    protected DatagramSocket getSocket() {
        return socket;
    }

    public abstract boolean addAddress(InetSocketAddress incomingAddress);

    protected abstract void removeExpiredAddresses();

    protected abstract void sendOutputData(DatagramSocket outputSocket) throws Exception;

    protected void handleNewInput(ByteBuffer inputData, InetSocketAddress inputAddress) {
        connectionMaintainer.receivedValidInputFromAddress(inputAddress);
    }
}
