package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.protocol.Input;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ClientStateManager {

    private int latestSequenceNumber;
    private byte[] clientInputs;

    public ClientStateManager(int inputLength) {
        this.latestSequenceNumber = 0;
        this.clientInputs = new byte[inputLength];
        Arrays.fill(clientInputs, Input.NO_INPUT);
    }


    public void setOutputData(ByteBuffer outputData) {
        Protocol.packageInputUpdate(outputData, this.latestSequenceNumber, this.clientInputs);
    }

    public boolean handleInput(ByteBuffer inputData) {
        return Protocol.validRoomUpdate(inputData);
    }
}
