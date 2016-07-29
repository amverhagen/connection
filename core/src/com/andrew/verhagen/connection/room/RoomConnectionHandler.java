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

    public RoomConnectionHandler(int maxPacketSizeInBytes, int maxConnections, int timeOutTimeInMilliseconds) {
        super(maxPacketSizeInBytes, maxConnections, timeOutTimeInMilliseconds);
    }

    @Override
    protected void resetHandler(DatagramSocket socket) throws SocketException {
        super.resetHandler(socket);
    }

    @Override
    public boolean addAddress(InetSocketAddress incomingAddress) {

//        if (activeConnectionAddresses.size() >= maxConnections || holdingConnection(incomingConnectionAddress))
//            return false;
//
//        activeConnectionAddresses.add(incomingConnectionAddress);
//
//        if (centerIsEmpty)
//            startCenter();
//
//        return true;
        return false;
    }

    @Override
    protected void removeExpiredAddresses() {

    }

    @Override
    protected void sendOutputData(DatagramSocket outputSocket) {

    }

    @Override
    protected boolean holdingConnection(ConnectionAddress incomingAddress) {
        return false;
    }

    @Override
    protected void handleNewInput(ByteBuffer inputData, ConnectionAddress inputAddress) {

    }
}
