package com.andrew.verhagen.connection.center;

import java.net.InetSocketAddress;

public class ConnectionAddress {

    public InetSocketAddress inetSocketAddress;
    public final long timeOutTimeInNanoSeconds;
    public long timeOfLastValidInput;

    public ConnectionAddress(InetSocketAddress destinationAddress, int timeoutTimeInMilliSeconds) {
        this(destinationAddress, (long) timeoutTimeInMilliSeconds * 1000000);
    }

    public ConnectionAddress(InetSocketAddress destinationAddress, long timeOutTimeInNanoSeconds) {
        this.timeOutTimeInNanoSeconds = timeOutTimeInNanoSeconds;
        this.inetSocketAddress = destinationAddress;
        this.timeOfLastValidInput = System.nanoTime();
    }

    public boolean hasSameAddressAndPort(ConnectionAddress incomingAddress) {
        return this.inetSocketAddress.getAddress().equals(incomingAddress.inetSocketAddress.getAddress())
                && this.inetSocketAddress.getPort() == incomingAddress.inetSocketAddress.getPort();
    }

    public boolean hasSameAddress(InetSocketAddress incomingAddress) {
        return this.inetSocketAddress.getAddress().equals(incomingAddress.getAddress());
    }

    public boolean hasSameAddressAndPort(InetSocketAddress incomingAddress) {
        return hasSameAddress(incomingAddress) && this.inetSocketAddress.getPort() == incomingAddress.getPort();
    }
}
