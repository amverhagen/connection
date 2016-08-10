package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.protocol.InputHandler;
import com.andrew.verhagen.connection.protocol.InputHandlerMapper;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class RoomPlayer extends ConnectionAddress implements InputHandler {

    private byte newestPlayerMessageState;
    private byte playerNumber;
    private byte playerColor;
    private InputTracker inputTracker;

    private RoomStateManager roomStateManager;

    private InputHandlerMapper inputHandlerMapper;

    public RoomPlayer(ConnectionAddress address, RoomStateManager roomStateManager, byte playerNumber) {
        super(address.inetSocketAddress, address.timeOutTimeInNanoSeconds);
        this.newestPlayerMessageState = Protocol.EMPTY;
        this.roomStateManager = roomStateManager;
        this.playerNumber = playerNumber;
        this.inputTracker = new InputTracker(Protocol.INPUT_LENGTH);

        this.inputHandlerMapper = new InputHandlerMapper(this);
    }

    public synchronized byte getPlayerNumber() {
        return this.playerNumber;
    }

    public synchronized byte getPlayerColor() {
        return this.playerColor;
    }

    public synchronized void handleInput(ByteBuffer inputData) {
        if (this.inputHandlerMapper.handleInput(inputData)) {
            System.out.println("Received valid input, resetting timer");
            this.timeOfLastValidInput = System.nanoTime();
        }

    }

    @Override
    public synchronized boolean handleWaitingForOpponentInput(ByteBuffer inputBuffer, byte incomingState) {
        try {
            if (inputBuffer.get() == playerNumber) {
                final byte color = inputBuffer.get();
                if (Protocol.isValidColor(color)) {
                    playerColor = color;
                    System.out.println("Player has valid number and color");
                    return updateNewestState(incomingState);
                }
            }
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized boolean handleOpponentFoundInput(ByteBuffer inputBuffer, byte incomingState) {
        return updateNewestState(incomingState);
    }

    @Override
    public synchronized boolean handleStartingInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public synchronized boolean handleInputUpdateInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public synchronized boolean handlePlayerOneWinInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public synchronized boolean handlePlayerTwoWinInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public synchronized boolean handleTieInput(ByteBuffer inputBuffer, byte incomingState) {
        return false;
    }

    @Override
    public synchronized boolean handlesMessageType(byte inputState) {
        return inputState <= roomStateManager.getOutputState() && inputState >= newestPlayerMessageState;
    }

    private synchronized boolean updateNewestState(byte incomingState) {
        if (incomingState >= newestPlayerMessageState) {
            newestPlayerMessageState = incomingState;
            return true;
        }
        return false;
    }

    public synchronized byte getNewestPlayerMessageState() {
        return newestPlayerMessageState;
    }

    public static boolean playerHasAddress(RoomPlayer player, InetSocketAddress address) {
        return (player != null && address != null && player.hasSameAddressAndPort(address));
    }
}
