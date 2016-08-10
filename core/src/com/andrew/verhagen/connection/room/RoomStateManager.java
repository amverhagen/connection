package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.protocol.OutputHandler;
import com.andrew.verhagen.connection.protocol.OutputHandlerMapper;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class RoomStateManager implements OutputHandler {

    private byte roomState;
    private int lowestCommonSequenceNumber;

    private OutputHandlerMapper outputHandlerMapper;

    private RoomPlayer playerOne;
    private RoomPlayer playerTwo;
    private RoomPlayer currentOutputPlayer;

    public RoomStateManager() {
        this.roomState = Protocol.EMPTY;
        this.playerOne = null;
        this.playerTwo = null;
        this.outputHandlerMapper = new OutputHandlerMapper(this);
    }

    public synchronized boolean connect(ConnectionAddress incomingAddress) {
        if (playerOne == null) {
            playerOne = new RoomPlayer(incomingAddress, this, Protocol.PLAYER_ONE);
            roomState = Protocol.WAITING_FOR_OPPONENT;
            return true;
        } else if (playerTwo == null) {
            playerTwo = new RoomPlayer(incomingAddress, this, Protocol.PLAYER_TWO);
            roomState = Protocol.OPPONENT_FOUND;
            return true;
        }
        return false;
    }

    public synchronized void disconnect(ConnectionAddress player) {
        if (playerOne != null && player.hasSameAddressAndPort(playerOne)) {
            roomState = Protocol.PLAYER_TWO_WIN;
        } else if (playerTwo != null && player.hasSameAddressAndPort(playerTwo)) {
            roomState = Protocol.PLAYER_ONE_WIN;
        }
    }

    public synchronized void handleInput(ByteBuffer inputData, InetSocketAddress address) {
        if (RoomPlayer.playerHasAddress(playerOne, address)) {
            playerOne.handleInput(inputData);
        } else if (RoomPlayer.playerHasAddress(playerTwo, address)) {
            playerTwo.handleInput(inputData);
        }
        this.setRoomStateBasedOnPlayerStates();
    }

    private synchronized boolean resetOutputPlayerAndReturn(boolean returnValue) {
        currentOutputPlayer = null;
        return returnValue;
    }

    private synchronized void setRoomStateBasedOnPlayerStates() {
        byte playerOneState = (playerOne != null) ? playerOne.getNewestPlayerMessageState() : Protocol.EMPTY;
        byte playerTwoState = (playerTwo != null) ? playerTwo.getNewestPlayerMessageState() : Protocol.EMPTY;
        byte oldestState = (playerOneState <= playerTwoState) ? playerOneState : playerTwoState;
        roomState = (oldestState > roomState) ? oldestState : roomState;
    }

    public synchronized boolean packOutputBuffer(ByteBuffer outputData, InetSocketAddress destination) {
        if (RoomPlayer.playerHasAddress(playerOne, destination)) {
            this.currentOutputPlayer = playerOne;
            return this.outputHandlerMapper.packOutput(outputData);
        } else if (RoomPlayer.playerHasAddress(playerTwo, destination)) {
            this.currentOutputPlayer = playerTwo;
            return this.outputHandlerMapper.packOutput(outputData);
        }
        return false;
    }

    @Override
    public synchronized boolean packWaitingForOpponentOutput(ByteBuffer outputBuffer) {
        try {
            outputBuffer.put(currentOutputPlayer.getPlayerNumber());
            return resetOutputPlayerAndReturn(true);
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
        }
        return resetOutputPlayerAndReturn(false);
    }

    @Override
    public synchronized boolean packOpponentFoundOutput(ByteBuffer outputBuffer) {
        byte opponentColor = (currentOutputPlayer.equals(playerOne)) ? playerOne.getPlayerColor() : playerTwo.getPlayerColor();
        outputBuffer.put(opponentColor);
        return resetOutputPlayerAndReturn(true);
    }

    @Override
    public synchronized boolean packGameStartingOutput(ByteBuffer outputBuffer) {
        return false;
    }

    @Override
    public synchronized boolean packInputUpdateOutput(ByteBuffer outputBuffer) {
        return false;
    }

    @Override
    public synchronized boolean packPlayerOneWinOutput(ByteBuffer outputBuffer) {
        return false;
    }

    @Override
    public synchronized boolean packPlayerTwoWinOutput(ByteBuffer outputBuffer) {
        return false;
    }

    @Override
    public synchronized boolean packTieOutput(ByteBuffer outputBuffer) {
        return false;
    }

    @Override
    public synchronized byte getOutputState() {
        return roomState;
    }
}
