package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.center.ConnectionAddress;
import com.andrew.verhagen.connection.center.ConnectionCenterHandler;
import com.andrew.verhagen.connection.protocol.InputHandlerMapper;
import com.andrew.verhagen.connection.protocol.OutputHandlerMapper;
import com.andrew.verhagen.connection.protocol.Protocol;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class ClientConnectionHandler extends ConnectionCenterHandler {

    private ConnectionAddress serverConnection;
    private ClientStateManager stateManager;
    private InputHandlerMapper inputHandlerMapper;
    private OutputHandlerMapper outputHandlerMapper;

    public ClientConnectionHandler(int maxPacketSizeInBytes, int maxConnections, int timeOutTimeInMilliseconds) {
        super(maxPacketSizeInBytes, maxConnections, timeOutTimeInMilliseconds);
    }


    @Override
    protected synchronized void resetHandler(DatagramSocket socket) throws SocketException {
        super.resetHandler(socket);
        serverConnection = null;
        stateManager = new ClientStateManager(Protocol.INPUT_LENGTH);
        inputHandlerMapper = new InputHandlerMapper(stateManager);
        outputHandlerMapper = new OutputHandlerMapper(stateManager);
    }

    @Override
    public synchronized boolean addAddress(InetSocketAddress incomingAddress) {
        if (holdingConnection(incomingAddress))
            return false;
        if (activeConnectionAddresses.size() < maxConnections) {
            serverConnection = new ConnectionAddress(incomingAddress, timeOutTimeInMilliseconds);
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
    protected synchronized void sendOutputData(DatagramSocket outputSocket) throws Exception {
        outputHandlerMapper.packOutput(outputData);
        outputPacket.setLength(outputData.position());
        outputPacket.setSocketAddress(serverConnection.inetSocketAddress);
        System.out.println("Sending packet to  " + serverConnection.inetSocketAddress);
        outputSocket.send(outputPacket);
    }

    @Override
    protected synchronized void handleNewInput(ByteBuffer inputData, InetSocketAddress inputAddress) {
        if (serverConnection.hasSameAddressAndPort(inputAddress))
            if (inputHandlerMapper.handleInput(inputData))
                serverConnection.timeOfLastValidInput = System.nanoTime();
    }
}
