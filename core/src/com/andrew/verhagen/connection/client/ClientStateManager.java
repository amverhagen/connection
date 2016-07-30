package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.server.GameServer;
import com.andrew.verhagen.connection.server.Input;

import java.nio.ByteBuffer;

public class ClientStateManager {


    public void setOutputData(ByteBuffer outputData) {
        outputData.clear();
        outputData.putInt(GameServer.PACKET_HEADER);
        outputData.putInt(1);
        for (int i = 0; i < 15; i++)
            outputData.put(Input.NO_INPUT);
    }
}
