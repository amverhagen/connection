package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.server.GameServer;
import com.andrew.verhagen.connection.server.Input;

import java.nio.ByteBuffer;

public class RoomStateManager {
    private int latestCommonSequence = 0;
    private int playerOneLatestSequence = 0;
    private int playerTwoLatestSequence = 0;
    private byte[] lastInputsPlayerOne;
    private byte[] lastInputsPlayerTwo;

    public RoomStateManager() {
        lastInputsPlayerOne = new byte[15];
        lastInputsPlayerTwo = new byte[15];
    }

    public boolean handleInputPlayerOne(ByteBuffer inputData) {
        try {
            if (inputData.getInt() == GameServer.PACKET_HEADER) {
                int playerOneInputSequence = inputData.getInt();
                if (playerOneInputSequence <= playerOneLatestSequence || playerOneInputSequence - 15 > latestCommonSequence)
                    return false;
                else {
                    inputData.mark();
                    byte input;
                    for (int i = 0; i < 15; i++) {
                        input = inputData.get();
                        int oldDataPosition = playerOneInputSequence - playerOneLatestSequence + i;
                        if (oldDataPosition < lastInputsPlayerOne.length) {
                            if (input != lastInputsPlayerOne[oldDataPosition])
                                return false;
                        } else if (!(input == Input.NO_INPUT || input == Input.LEFT || input == Input.RIGHT)) {
                            return false;
                        }
                    }
                    inputData.reset();
                    inputData.get(lastInputsPlayerOne, 0, lastInputsPlayerOne.length);
                    playerOneLatestSequence = playerOneInputSequence;
                    return true;
                }
            }
        } catch (Exception e) {
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
