package com.andrew.verhagen.connection.room;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.center.ConnectionCenterHandler;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class RoomConnectionHandler extends ConnectionCenterHandler {

    private RoomStateManager stateManager;
    private String tag = "RoomConnectionHandler: ";


    public RoomConnectionHandler(int maxPacketSizeInBytes, int maxConnections, int timeOutTimeInMilliseconds) {
        super(maxPacketSizeInBytes, maxConnections, timeOutTimeInMilliseconds);
        this.stateManager = new RoomStateManager();
    }

    @Override
    protected synchronized void resetHandler(DatagramSocket socket) throws SocketException {
        super.resetHandler(socket);
        stateManager = new RoomStateManager();
    }

    @Override
    public synchronized boolean addAddress(InetSocketAddress incomingAddress) {
        if (holdingConnection(incomingAddress))
            return false;
        if (activeConnectionAddresses.size() >= maxConnections)
            return false;

        //TODO Fix this.
        ConnectionAddress incomingConnectionAddress = new ConnectionAddress(incomingAddress, timeOutTimeInMilliseconds);
        if (stateManager.connect(incomingConnectionAddress)) {
            activeConnectionAddresses.add(incomingConnectionAddress);
            return true;
        }
        return false;
    }

    @Override
    protected synchronized void removeExpiredAddresses() {
        System.err.println(tag + "Expired connections");
        for (ConnectionAddress connectionAddress : expiredConnectionAddresses) {
            activeConnectionAddresses.remove(connectionAddress);
            stateManager.disconnect(connectionAddress);
        }
        if (activeConnectionAddresses.size() < 1)
            socket.close();
    }

    @Override
    protected synchronized void sendOutputData(DatagramSocket outputSocket) throws Exception {

        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            stateManager.packOutputBuffer(outputData, connectionAddress.inetSocketAddress);
            outputPacket.setLength(outputData.position());
            outputPacket.setSocketAddress(connectionAddress.inetSocketAddress);
            System.out.println(tag + "Sending packet to " + connectionAddress.inetSocketAddress);
            outputSocket.send(outputPacket);
        }
    }

    @Override
    protected synchronized void handleNewInput(ByteBuffer inputData, InetSocketAddress inputAddress) {
        stateManager.handleInput(inputData, inputAddress);
    }
}
