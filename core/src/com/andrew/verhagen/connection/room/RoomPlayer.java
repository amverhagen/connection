package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class RoomPlayer extends ConnectionAddress {

    private int latestInputSequence;
    private byte[] lastInputs;
    protected boolean connected;

    public RoomPlayer(InetSocketAddress inetSocketAddress, int timeOutTimeInMilliSeconds) {
        super(inetSocketAddress.getAddress(), inetSocketAddress.getPort(), timeOutTimeInMilliSeconds);
        latestInputSequence = 0;
        lastInputs = new byte[15];
        connected = true;
    }

    public void handleInput(ByteBuffer inputData) throws BufferUnderflowException {
        if (Protocol.validHeader(inputData))
            this.timeOfLastInput = System.nanoTime();
    }
}
