package com.andrew.verhagen.connection.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class Protocol {
    public static final int PACKET_HEADER = 539837401;
    public static final int ROOM_REQUEST = 201;
    public static final int ACCEPTED_ROOM_REQUEST_HEADER = 401;

    public static final boolean validHeader(ByteBuffer inputData) throws BufferUnderflowException {
        inputData.position(0);
        return inputData.getInt() == PACKET_HEADER;
    }

    public static final void packageHeader(ByteBuffer outputData) {
        outputData.clear();
        outputData.position(0);
        outputData.putInt(PACKET_HEADER);
    }

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

    public static final boolean validRoomResponse(ByteBuffer inputData) throws BufferUnderflowException {
        if (validHeader(inputData))
            if (inputData.getInt() == ACCEPTED_ROOM_REQUEST_HEADER)
                return true;
        return false;
    }

    public static final void packageAcceptedRoomRequest(ByteBuffer outputData) {
        packageHeader(outputData);
        outputData.putInt(ACCEPTED_ROOM_REQUEST_HEADER);
    }
}
