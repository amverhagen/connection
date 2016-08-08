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
    }

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
