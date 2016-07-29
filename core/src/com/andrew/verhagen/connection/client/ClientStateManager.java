package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.server.GameServer;

import java.nio.ByteBuffer;

public class ClientStateManager {


    public void setOutputData(ByteBuffer outputData) {
        outputData.clear();
        outputData.putInt(GameServer.PACKET_HEADER);
    }
}
