package com.andrew.verhagen.connection.protocol;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class InputHandlerMapper {

    private final InputHandler handler;

    public InputHandlerMapper(InputHandler handler) {
        this.handler = handler;
    }

    public boolean handleInput(ByteBuffer inputBuffer) {
        try {
            if (Protocol.validHeader(inputBuffer)) {
                final byte inputState = inputBuffer.get();
                synchronized (handler) {
                    if (handler.handlesMessageType(inputState)) {
                        if (inputState == Protocol.WAITING_FOR_OPPONENT) {
                            return handler.handleWaitingForOpponentInput(inputBuffer, inputState);
                        } else if (inputState == Protocol.OPPONENT_FOUND) {
                            return handler.handleOpponentFoundInput(inputBuffer, inputState);
                        } else if (inputState == Protocol.STARTING) {
                            return handler.handleStartingInput(inputBuffer, inputState);
                        } else if (inputState == Protocol.INPUT_UPDATE) {
                            return handler.handleInputUpdateInput(inputBuffer, inputState);
                        } else if (inputState == Protocol.PLAYER_ONE_WIN) {
                            return handler.handlePlayerOneWinInput(inputBuffer, inputState);
                        } else if (inputState == Protocol.PLAYER_TWO_WIN) {
                            return handler.handlePlayerTwoWinInput(inputBuffer, inputState);
                        } else if (inputState == Protocol.TIE) {
                            return handler.handleTieInput(inputBuffer, inputState);
                        }
                    }
                }
            }
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
        }
        return false;
    }
}
