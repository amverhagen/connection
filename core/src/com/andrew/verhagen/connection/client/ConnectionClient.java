package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.center.ConnectionCenter;
import com.andrew.verhagen.connection.room.ConnectionState;
import com.andrew.verhagen.connection.server.GameServer;
import com.andrew.verhagen.line.gambit.network.Protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

public class ConnectionClient {

    private ConnectionState connectionState;
    private ClientConnectionHandler handler;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;

    public ConnectionClient(InetAddress serverAddress, int serverPort) {
        this.connectionState = ConnectionState.NOT_CONNECTED;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void connectToServer() {
        if (getConnectionState() == ConnectionState.NOT_CONNECTED) {
            establishConnection();
        }
    }

    private void establishConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteBuffer serverData = ByteBuffer.allocate(4);
                DatagramPacket serverPacket = new DatagramPacket(serverData.array(), 0, serverData.capacity());
                try {
                    endClientConnection();
                    socket = new DatagramSocket();
                    socket.setSoTimeout(2000);
                    for (int i = 0; i < 10; i++) {
                        setConnectionState(ConnectionState.CONNECTING);
                        packageOutput(serverData, serverPacket);
                        try {
                            socket.send(serverPacket);
                            socket.receive(serverPacket);
                            if (Protocol.validInput(serverData)) {
                                setConnectionState(ConnectionState.CONNECTED);
                                handler = new ClientConnectionHandler(256, 1, 2000);
                                new ConnectionCenter(handler, socket, (InetSocketAddress) serverPacket.getSocketAddress());
                                return;
                            }
                        } catch (SocketTimeoutException ste) {
                            System.out.println("Connection timed out, retrying");
                        }
                    }
                } catch (IOException e) {
                }
                endClientConnection();
            }
        }).start();
    }

    //TODO create request protocol.
    private void packageOutput(ByteBuffer outputData, DatagramPacket outputPacket) {
        outputPacket.setAddress(serverAddress);
        outputPacket.setPort(serverPort);
        outputData.clear();
        outputData.putInt(GameServer.PACKET_HEADER);
    }

    private void setConnectionState(ConnectionState connectionState) {
        synchronized (connectionState) {
            this.connectionState = connectionState;
        }
    }

    public ConnectionState getConnectionState() {
        synchronized (connectionState) {
            return connectionState;
        }
    }

    public synchronized void endClientConnection() {
        this.setConnectionState(ConnectionState.FAILED);
        if (socket != null)
            socket.close();
        System.out.println("Connection ended");
    }
}
