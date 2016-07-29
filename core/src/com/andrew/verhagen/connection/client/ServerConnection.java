package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.center.ConnectionAddress;

import java.net.InetAddress;

public class ServerConnection extends ConnectionAddress {

    public ServerConnection(InetAddress destinationAddress, int destinationPort, int timeOutTimeInMilliSeconds) {
        super(destinationAddress, destinationPort, timeOutTimeInMilliSeconds);
    }
}
