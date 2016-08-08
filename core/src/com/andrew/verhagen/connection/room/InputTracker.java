package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.protocol.Input;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class InputTracker {

    private byte[] savedInputs;
    private int currentSequenceNumber;
    private byte[] incomingInputs;
    private int incomingSequenceNumber;


    public InputTracker(int numberOfInputs) {
        this.currentSequenceNumber = 0;
        this.savedInputs = new byte[numberOfInputs];
        this.incomingInputs = new byte[numberOfInputs];
        Arrays.fill(this.savedInputs, Input.NOT_RECEIVED);
    }

    public boolean updateInput(ByteBuffer incomingData) {
        incomingSequenceNumber = incomingData.getInt();
        if (incomingSequenceNumber <= currentSequenceNumber)
            return false;
        incomingData.get(incomingInputs);
        if (!Input.isValidInput(incomingInputs))
            return false;
        return updateInputs(incomingSequenceNumber, incomingInputs);
    }

    private boolean updateInputs(int sequenceNumber, byte[] incomingInputs) {
        return false;
    }
}
