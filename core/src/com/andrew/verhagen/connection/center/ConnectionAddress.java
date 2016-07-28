package com.andrew.verhagen.connection.center;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class ConnectionAddress {

    public InetSocketAddress connectionAddress;
    public final long timeOutTimeInNanoSeconds;
    public long timeOfLastInput;

    public ConnectionAddress(InetAddress destinationAddress, int destinationPort, int timeOutTimeInMilliSeconds) {
        this(destinationAddress, destinationPort, (long) timeOutTimeInMilliSeconds * 1000000);
    }

    public ConnectionAddress(InetAddress destinationAddress, int destinationPort, long timeOutTimeInNanoSeconds) {
        this.timeOutTimeInNanoSeconds = timeOutTimeInNanoSeconds;
        this.timeOfLastInput = System.nanoTime();
        this.connectionAddress = new InetSocketAddress(destinationAddress, destinationPort);
    }

    public boolean hasSameAddressAndPort(InetAddress address, int port) {
        return this.connectionAddress.getAddress().equals(address) && this.connectionAddress.getPort() == port;
    }
}
