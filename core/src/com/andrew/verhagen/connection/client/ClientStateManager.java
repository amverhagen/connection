package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.protocol.Protocol;

import java.nio.ByteBuffer;

public class ClientStateManager {

    public void setOutputData(ByteBuffer outputData) {
        Protocol.packageHeader(outputData);
    }

    public boolean handleInput(ByteBuffer inputData) {
        return Protocol.validRoomUpdate(inputData);
    }
}
