package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;

import java.net.InetAddress;

public class RoomPlayer extends ConnectionAddress {
    public RoomPlayer(InetAddress destinationAddress, int destinationPort, int timeOutTimeInMilliSeconds) {
        super(destinationAddress, destinationPort, timeOutTimeInMilliSeconds);
    }
}
