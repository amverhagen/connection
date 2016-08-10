package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.protocol.Input;
import com.andrew.verhagen.connection.protocol.InputHandler;
import com.andrew.verhagen.connection.protocol.OutputHandler;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ClientStateManager implements InputHandler, OutputHandler {

    private int remoteSequenceNumber;
    private int localSequenceNumber;
    private byte[] clientInputs;
    private byte serverState;
    private byte playerNumber;
    private byte playerColor = Protocol.WHITE;

    public ClientStateManager(int inputLength) {
        this.remoteSequenceNumber = 0;
        this.localSequenceNumber = 0;
        this.clientInputs = new byte[inputLength];
        this.serverState = Protocol.EMPTY;
        Arrays.fill(clientInputs, Input.NO_INPUT);
    }

    private synchronized boolean updateState(byte newState) {
        if (newState <= this.serverState)
            return false;
        this.serverState = newState;
        return true;
    }

    @Override
    public boolean handleWaitingForOpponentInput(ByteBuffer inputBuffer, byte incomingState) {
        try {
            final byte playerNum = inputBuffer.get();
            if (Protocol.isValidPlayerNumber(playerNum)) {
                this.playerNumber = playerNum;
                return updateState(incomingState);
            }
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean handleOpponentFoundInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public boolean handleStartingInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public boolean handleInputUpdateInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public boolean handlePlayerOneWinInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public boolean handlePlayerTwoWinInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public boolean handleTieInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public boolean handlesMessageType(byte inputState) {
        return inputState >= serverState;
    }

    @Override
    public boolean packWaitingForOpponentOutput(ByteBuffer outputBuffer) {
        try {
            outputBuffer.put(playerNumber);
            outputBuffer.put(playerColor);
            return true;
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean packOpponentFoundOutput(ByteBuffer inputBuffer) {
        return false;
    }

    @Override
    public boolean packGameStartingOutput(ByteBuffer inputBuffer) {
        return false;
    }

    @Override
    public boolean packInputUpdateOutput(ByteBuffer inputBuffer) {
        return false;
    }

    @Override
    public boolean packPlayerOneWinOutput(ByteBuffer inputBuffer) {
        return false;
    }

    @Override
    public boolean packPlayerTwoWinOutput(ByteBuffer inputBuffer) {
        return false;
    }

    @Override
    public boolean packTieOutput(ByteBuffer inputBuffer) {
        return false;
    }

    @Override
    public synchronized byte getOutputState() {
        return serverState;
    }
}
