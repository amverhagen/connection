package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.center.ConnectionCenterHandler;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class ClientConnectionHandler extends ConnectionCenterHandler {

    private ServerConnection serverConnection;
    private ClientStateManager stateManager;

    public ClientConnectionHandler(int maxPacketSizeInBytes, int maxConnections, int timeOutTimeInMilliseconds) {
        super(maxPacketSizeInBytes, maxConnections, timeOutTimeInMilliseconds);
    }


    @Override
    protected synchronized void resetHandler(DatagramSocket socket) throws SocketException {
        super.resetHandler(socket);
        serverConnection = null;
        stateManager = new ClientStateManager();
    }

    @Override
    public synchronized boolean addAddress(InetSocketAddress incomingAddress) {
        if (holdingConnection(incomingAddress))
            return false;
        if (activeConnectionAddresses.size() < maxConnections) {
            serverConnection = new ServerConnection(incomingAddress.getAddress(), incomingAddress.getPort(), timeOutTimeInMilliseconds);
            activeConnectionAddresses.add(serverConnection);
            return true;
        }
        return false;
    }

    @Override
    protected void removeExpiredAddresses() {
        System.err.println("Expired connections");
        socket.close();
    }

    @Override
    protected void sendOutputData(DatagramSocket outputSocket) throws Exception {

        for (ConnectionAddress connectionAddress : activeConnectionAddresses) {
            stateManager.setOutputData(outputData);
            outputPacket.setLength(outputData.position());
            outputPacket.setSocketAddress(connectionAddress.connectionAddress);
            System.out.println("Set socket address to " + connectionAddress.connectionAddress);
            outputSocket.send(outputPacket);
        }
    }

    @Override
    protected void handleNewInput(ByteBuffer inputData, InetSocketAddress inputAddress) {
        System.out.println("Client received data");
        //Set last input time.
    }
}
