package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class RoomPlayer extends ConnectionAddress {

    protected boolean connected;
    private InputTracker inputTracker;

    public RoomPlayer(InetSocketAddress inetSocketAddress, int timeOutTimeInMilliSeconds) {
        super(inetSocketAddress.getAddress(), inetSocketAddress.getPort(), timeOutTimeInMilliSeconds);
        this.inputTracker = new InputTracker(15);
        this.connected = true;
    }

    public void handleInput(ByteBuffer inputData) throws BufferUnderflowException {
        if (inputTracker.updateInput(inputData))
            this.timeOfLastValidInput = System.nanoTime();
    }
}
