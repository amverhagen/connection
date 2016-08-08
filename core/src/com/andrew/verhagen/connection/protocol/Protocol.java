package com.andrew.verhagen.connection.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class Protocol {
    public static final int PACKET_HEADER = 539837401;
    public static final int ROOM_REQUEST = 201;
    public static final byte INPUT_UPDATE = 11;

    //Header protocol
    public static final boolean validHeader(ByteBuffer inputData) throws BufferUnderflowException {
        inputData.position(0);
        return inputData.getInt() == PACKET_HEADER;
    }

    public static final void packageHeader(ByteBuffer outputData) {
        outputData.clear();
        outputData.position(0);
        outputData.putInt(PACKET_HEADER);
    }

    //Room Request protocol.
    public static final boolean validRoomRequest(ByteBuffer inputData) throws BufferUnderflowException {
        if (validHeader(inputData))
            if (inputData.getInt() == ROOM_REQUEST)
                return true;
        return false;
    }

    public static final void packageRoomRequest(ByteBuffer outputData) {
        packageHeader(outputData);
        outputData.putInt(ROOM_REQUEST);
    }

    //TODO specify this into roomStateInputUpdate, roomStateWaitingOnOpponentUpdate...
    //Room state protocol
    public static final boolean validRoomUpdate(ByteBuffer inputData) {
        if (validHeader(inputData))
            return true;
        return false;
    }

    public static final void packageRoomStateUpdate(ByteBuffer outputData, byte roomState) {
        packageHeader(outputData);
        outputData.put(roomState);
    }

    //Input Update
    public static final boolean validInputUpdate(ByteBuffer inputData) {
        if (validHeader(inputData))
            if (inputData.get() == INPUT_UPDATE)
                return true;
        return false;
    }

    public static final void packageInputUpdate(ByteBuffer outputData, int sequenceNumber, byte[] data) {
        packageHeader(outputData);
        outputData.put(INPUT_UPDATE);
    }
}
