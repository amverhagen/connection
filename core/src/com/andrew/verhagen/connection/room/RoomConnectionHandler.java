package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.center.ConnectionCenterHandler;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class RoomConnectionHandler extends ConnectionCenterHandler {

    private RoomPlayer playerOne;
    private RoomPlayer playerTwo;
    private RoomStateManager stateManager;

    public RoomConnectionHandler(int maxPacketSizeInBytes, int maxConnections, int timeOutTimeInMilliseconds) {
        super(maxPacketSizeInBytes, maxConnections, timeOutTimeInMilliseconds);
        this.stateManager = new RoomStateManager();
    }

    @Override
    protected synchronized void resetHandler(DatagramSocket socket) throws SocketException {
        super.resetHandler(socket);
        playerOne = null;
        playerTwo = null;
        stateManager = new RoomStateManager();
    }

    @Override
    public synchronized boolean addAddress(InetSocketAddress incomingAddress) {
        if (holdingConnection(incomingAddress))
            return false;
        if (playerOne == null) {
            System.out.println("Added player");
            playerOne = new RoomPlayer(incomingAddress, timeOutTimeInMilliseconds);
            activeConnectionAddresses.add(playerOne);
        } else if (playerTwo == null) {
            playerTwo = new RoomPlayer(incomingAddress, timeOutTimeInMilliseconds);
            activeConnectionAddresses.add(playerTwo);
        } else return false;
        return true;
    }

    @Override
    protected synchronized void removeExpiredAddresses() {
        System.err.println("Expired connections");
        for (ConnectionAddress connectionAddress : expiredConnectionAddresses) {
            if (connectionAddress.hasSameAddressAndPort(playerOne)) {
                System.out.println("Removed player one.");
                activeConnectionAddresses.remove(playerOne);
                playerOne = null;
            } else if (connectionAddress.hasSameAddressAndPort(playerTwo)) {
                activeConnectionAddresses.remove(playerTwo);
                playerTwo = null;
            }
        }
        if (activeConnectionAddresses.size() < 1)
            socket.close();
    }

    @Override
    protected synchronized void sendOutputData(DatagramSocket outputSocket) throws Exception {
        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            stateManager.setOutputData(outputData);
            outputPacket.setLength(outputData.position());
            outputPacket.setSocketAddress(connectionAddress.connectionAddress);
            System.out.println("Set socket address to " + connectionAddress.connectionAddress);
            outputSocket.send(outputPacket);
        }
    }

    @Override
    protected synchronized void handleNewInput(ByteBuffer inputData, InetSocketAddress inputAddress) {
        long validReceptionTime = System.nanoTime();
        if (playerOne.hasSameAddressAndPort(inputAddress)) {
            if (stateManager.handleInputPlayerOne(inputData))
                playerOne.timeOfLastInput = validReceptionTime;
        } else if (playerTwo.hasSameAddressAndPort(inputAddress)) {
            if (stateManager.handleInputPlayerTwo(inputData))
                playerTwo.timeOfLastInput = validReceptionTime;
        }
    }
}
