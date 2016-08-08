package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.protocol.Input;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class InputTracker {

    private final byte[] savedInputs;
    private int savedSequenceNumber;
    private final byte[] incomingInputs;
    private int incomingSequenceNumber;


    public InputTracker(int numberOfInputs) {
        this.savedSequenceNumber = 0;
        this.savedInputs = new byte[numberOfInputs];
        this.incomingInputs = new byte[numberOfInputs];
        Arrays.fill(this.savedInputs, Input.NOT_RECEIVED);
    }

    public synchronized boolean updateInput(ByteBuffer incomingData) {
        if (validInput(incomingData)) {
            updateSavedInputs();
            return true;
        }
        return false;
    }

    private boolean validInput(ByteBuffer data) {
        try {

            if (!Protocol.validInputUpdate(data))
                return false;

            incomingSequenceNumber = data.getInt();
            if (incomingSequenceNumber <= savedSequenceNumber
                    || (incomingSequenceNumber > (savedSequenceNumber + savedInputs.length)))
                return false;

            data.get(incomingInputs);
            return Input.isValidInput(incomingInputs) && consistentWithSavedData();

        } catch (BufferUnderflowException e) {
            return false;
        }
    }

    private boolean consistentWithSavedData() {
        int difference = incomingSequenceNumber - savedSequenceNumber;

        for (int i = 0; i < savedInputs.length - difference; i++) {
            if (savedInputs[i] != incomingInputs[i + difference])
                return false;
        }
        return true;
    }

    private void updateSavedInputs() {
        System.arraycopy(this.incomingInputs, 0, this.savedInputs, 0, this.savedInputs.length);
        this.savedSequenceNumber = this.incomingSequenceNumber;
    }
}
