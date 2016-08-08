package com.andrew.verhagen.connection.center;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ConnectionAddress {

    public InetSocketAddress connectionAddress;
    public final long timeOutTimeInNanoSeconds;
    public long timeOfLastValidInput;

    public ConnectionAddress(InetAddress destinationAddress, int destinationPort, int timeOutTimeInMilliSeconds) {
        this(destinationAddress, destinationPort, (long) timeOutTimeInMilliSeconds * 1000000);
    }

    public ConnectionAddress(InetAddress destinationAddress, int destinationPort, long timeOutTimeInNanoSeconds) {
        this.timeOutTimeInNanoSeconds = timeOutTimeInNanoSeconds;
        this.timeOfLastValidInput = System.nanoTime();
        this.connectionAddress = new InetSocketAddress(destinationAddress, destinationPort);
    }

    public boolean hasSameAddressAndPort(ConnectionAddress incomingAddress) {
        return this.connectionAddress.getAddress().equals(incomingAddress.connectionAddress.getAddress())
                && this.connectionAddress.getPort() == incomingAddress.connectionAddress.getPort();
    }

    public boolean hasSameAddress(InetSocketAddress incomingAddress) {
        return this.connectionAddress.getAddress().equals(incomingAddress.getAddress());
    }

    public boolean hasSameAddressAndPort(InetSocketAddress incomingAddress) {
        return hasSameAddress(incomingAddress) && this.connectionAddress.getPort() == incomingAddress.getPort();
    }
}
