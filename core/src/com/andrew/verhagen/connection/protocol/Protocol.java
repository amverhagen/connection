package com.andrew.verhagen.connection.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class Protocol {

    public static final byte WHITE = 1;
    public static final byte RED = 2;
    public static final byte GREEN = 3;
    public static final byte BLUE = 4;

    public static final int INPUT_LENGTH = 15;
    public static final int PACKET_HEADER = 539837401;
    public static final int ROOM_REQUEST = 201;
    public static final byte INPUT_UPDATE = 88;

    public static final byte PLAYER_ONE = (byte) 1;
    public static final byte PLAYER_TWO = (byte) 2;

    //Starting up states 10-19;
    public static final byte EMPTY = (byte) 10;
    public static final byte WAITING_FOR_OPPONENT = (byte) 11;
    public static final byte OPPONENT_FOUND = (byte) 12;
    public static final byte STARTING = (byte) 13;

    //Running states 20-29;
    public static final byte RUNNING = (byte) 20;
//    public static final byte INPUT_UPDATE = (byte) 21;

    //Game over states 30-39;
    public static final byte GAME_OVER = (byte) 30;
    public static final byte PLAYER_ONE_WIN = (byte) 31;
    public static final byte PLAYER_TWO_WIN = (byte) 32;
    public static final byte TIE = (byte) 33;


    public static void packageHeader(ByteBuffer outputData) {
        outputData.clear();
        outputData.position(0);
        outputData.putInt(PACKET_HEADER);
    }

    public static void packMessage(ByteBuffer outputBuffer, byte messageType) {
        packageHeader(outputBuffer);
        outputBuffer.put(messageType);
    }

    //Header protocol
    public static boolean validHeader(ByteBuffer inputData) throws BufferUnderflowException {
        inputData.position(0);
        return inputData.getInt() == PACKET_HEADER;
    }


    //Room Request protocol.
    public static boolean validRoomRequest(ByteBuffer inputData) throws BufferUnderflowException {
        if (validHeader(inputData))
            if (inputData.getInt() == ROOM_REQUEST)
                return true;
        return false;
    }

    public static void packageRoomRequest(ByteBuffer outputData) {
        packageHeader(outputData);
        outputData.putInt(ROOM_REQUEST);
    }

    //Input Update
    public static boolean validInputUpdate(ByteBuffer inputData) {
        if (validHeader(inputData))
            if (inputData.get() == INPUT_UPDATE)
                return true;
        return false;
    }

    public static boolean isValidColor(byte color) {
        return (color == WHITE || color == RED || color == GREEN || color == BLUE);
    }

    public static boolean isValidPlayerNumber(byte playerNumber) {
        return (playerNumber == PLAYER_ONE || playerNumber == PLAYER_TWO);
    }
}

//    public static void packageInputUpdate(ByteBuffer outputData, int sequenceNumber, byte[] data) {
//        packageHeader(outputData);
//        outputData.put(INPUT_UPDATE);
//        outputData.putInt(sequenceNumber);
//        outputData.put(data, 0, data.length);
//    }
