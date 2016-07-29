package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;

import java.net.InetSocketAddress;

public class RoomPlayer extends ConnectionAddress {
    public RoomPlayer(InetSocketAddress inetSocketAddress, int timeOutTimeInMilliSeconds) {
        super(inetSocketAddress.getAddress(), inetSocketAddress.getPort(), timeOutTimeInMilliSeconds);
    }
}
