package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class RoomStateManager {
    private byte roomState = RoomState.EMPTY;
    private int latestCommonSequence = 0;
    private RoomPlayer playerOne;
    private RoomPlayer playerTwo;

    public RoomStateManager() {
//        lastInputsPlayerOne = new byte[15];
//        Arrays.fill(lastInputsPlayerOne, Input.NOT_RECEIVED);
//        lastInputsPlayerTwo = new byte[15];
//        Arrays.fill(lastInputsPlayerTwo, Input.NOT_RECEIVED);
    }
//
//    public boolean handleInputPlayerOne(ByteBuffer inputData) {
//        try {
//            if (Protocol.validHeader(inputData)) {
//                int playerOneIncomingInputSequenceNumber = inputData.getInt();
//                System.out.format("Received input sequence %d \n", playerOneIncomingInputSequenceNumber);
//                if (playerOneIncomingInputSequenceNumber < playerOneLatestSequence || playerOneIncomingInputSequenceNumber - 15 > latestCommonSequence) {
//                    System.out.format("Received input %d is not new\n", playerOneIncomingInputSequenceNumber);
//                    return false;
//                } else {
//                    inputData.mark();
//                    byte incomingData;
//                    for (int i = 0; i < 15; i++) {
//                        incomingData = inputData.get();
//                        if (!(incomingData == Input.NO_INPUT || incomingData == Input.LEFT || incomingData == Input.RIGHT)) {
//                            System.out.format("Incoming data %d is not valid\n", incomingData);
//                            return false;
//                        }
//
//                        int oldDataPosition = playerOneIncomingInputSequenceNumber - playerOneLatestSequence + i;
//                        if (oldDataPosition >= 15)
//                            continue;
//                        byte establishedData = lastInputsPlayerOne[oldDataPosition];
//                        if (establishedData == Input.NOT_RECEIVED)
//                            continue;
//                        if (incomingData != establishedData)
//                            return false;
//                    }
//                    inputData.reset();
//                    inputData.get(lastInputsPlayerOne, 0, lastInputsPlayerOne.length);
//                    playerOneLatestSequence = playerOneIncomingInputSequenceNumber;
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
//
//    public boolean handleInputPlayerTwo(ByteBuffer inputData) {
//        return false;
//    }
//
//    public void setOutputData(ByteBuffer outputData) {
//        Protocol.packageHeader(outputData);
//    }


    public void setOutputData(ByteBuffer outputData) {
        Protocol.packageRoomStateUpdate(outputData, getRoomState());
    }

    public synchronized void handleInput(ByteBuffer inputData, InetSocketAddress address) {
        try {
            if (playerOne.hasSameAddressAndPort(address)) {
                playerOne.handleInput(inputData);
            } else if (playerTwo.hasSameAddressAndPort(address)) {
                playerTwo.handleInput(inputData);
            }
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
        }
    }

    public synchronized void disconnect(ConnectionAddress player) {
        if (playerOne != null && player.hasSameAddressAndPort(playerOne)) {
            playerOne.connected = false;
        } else if (playerTwo != null && player.hasSameAddressAndPort(playerTwo))
            playerTwo.connected = false;
        updateRoomState();
    }

    public synchronized boolean connect(RoomPlayer player) {
        if (playerOne == null) {
            playerOne = player;
            updateRoomState();
            return true;
        } else if (playerTwo == null) {
            playerTwo = player;
            updateRoomState();
            return true;
        } else {
            return false;
        }
    }

    private synchronized byte getRoomState() {
        return roomState;
    }

    private synchronized void updateRoomState() {
        if ((playerOne != null && playerOne.connected) && (playerTwo != null && playerTwo.connected)) {
            roomState = RoomState.FULL;
        } else if ((playerOne != null && playerOne.connected) || (playerTwo != null && playerTwo.connected)) {
            roomState = RoomState.ONE_PLAYER;
        } else
        //Notify connection handler that room is empty
            roomState = RoomState.EMPTY;
    }
}
