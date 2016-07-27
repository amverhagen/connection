package com.andrew.verhagen.connection.server;

import java.net.InetAddress;

public class ConnectionAddress {

    public final InetAddress destinationAddress;
    public final int destinationPort;
    public final long timeOutTimeInNanoSeconds;

    public ConnectionAddress(InetAddress destinationAddress, int destinationPort, int timeOutTimeInMilliSeconds) {
        this.destinationAddress = destinationAddress;
        this.destinationPort = destinationPort;
        this.timeOutTimeInNanoSeconds = (long) timeOutTimeInMilliSeconds * 1000000;
    }

    public ConnectionAddress(InetAddress destinationAddress, int destinationPort, long timeOutTimeInNanoSeconds) {
        this.destinationAddress = destinationAddress;
        this.destinationPort = destinationPort;
        this.timeOutTimeInNanoSeconds = timeOutTimeInNanoSeconds;
    }

    public boolean hasSameAddressAndPort(InetAddress address, int port) {
        return this.destinationAddress.equals(address) && this.destinationPort == port;
    }
}
