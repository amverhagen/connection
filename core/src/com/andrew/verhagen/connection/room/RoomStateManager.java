package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.server.GameServer;
import com.andrew.verhagen.connection.server.Input;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class RoomStateManager {
    private int latestCommonSequence = 0;
    private int playerOneLatestSequence = 0;
    private int playerTwoLatestSequence = 0;
    private byte[] lastInputsPlayerOne;
    private byte[] lastInputsPlayerTwo;

    public RoomStateManager() {
        lastInputsPlayerOne = new byte[15];
        Arrays.fill(lastInputsPlayerOne, Input.NOT_RECEIVED);
        lastInputsPlayerTwo = new byte[15];
        Arrays.fill(lastInputsPlayerTwo, Input.NOT_RECEIVED);
    }

    public boolean handleInputPlayerOne(ByteBuffer inputData) {
        try {
            if (inputData.getInt() == GameServer.PACKET_HEADER) {
                int playerOneIncomingInputSequenceNumber = inputData.getInt();
                System.out.format("Received input sequence %d \n", playerOneIncomingInputSequenceNumber);
                if (playerOneIncomingInputSequenceNumber < playerOneLatestSequence || playerOneIncomingInputSequenceNumber - 15 > latestCommonSequence) {
                    System.out.format("Received input %d is not new\n", playerOneIncomingInputSequenceNumber);
                    return false;
                } else {
                    inputData.mark();
                    byte incomingData;
                    for (int i = 0; i < 15; i++) {
                        incomingData = inputData.get();
                        if (!(incomingData == Input.NO_INPUT || incomingData == Input.LEFT || incomingData == Input.RIGHT)) {
                            System.out.format("Incoming data %d is not valid\n", incomingData);
                            return false;
                        }

                        int oldDataPosition = playerOneIncomingInputSequenceNumber - playerOneLatestSequence + i;
                        if (oldDataPosition >= 15)
                            continue;
                        byte establishedData = lastInputsPlayerOne[oldDataPosition];
                        if (establishedData == Input.NOT_RECEIVED)
                            continue;
                        if (incomingData != establishedData)
                            return false;
                    }
                    inputData.reset();
                    inputData.get(lastInputsPlayerOne, 0, lastInputsPlayerOne.length);
                    playerOneLatestSequence = playerOneIncomingInputSequenceNumber;
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean handleInputPlayerTwo(ByteBuffer inputData) {
        return false;
    }

    public void setOutputData(ByteBuffer outputData) {
        outputData.clear();
        outputData.putInt(GameServer.PACKET_HEADER);
    }
}
