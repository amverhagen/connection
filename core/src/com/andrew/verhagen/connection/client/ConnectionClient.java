package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.center.ConnectionCenter;
import com.andrew.verhagen.connection.protocol.Protocol;
import com.andrew.verhagen.connection.protocol.ConnectionState;
import com.andrew.verhagen.line.gambit.systems.matchmaking.ConnectionObserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ConnectionClient {

    private ConnectionState connectionState;
    private ClientConnectionHandler handler;
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private static final String SERVER_ADDRESS = "192.168.0.4";
    private static final int SERVER_PORT = 9001;
    private ArrayList<ConnectionObserver> connectionObservers;


    public ConnectionClient() {
        this.connectionState = ConnectionState.NOT_CONNECTED;
        try {
            this.serverAddress = InetAddress.getByName(SERVER_ADDRESS);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        connectionObservers = new ArrayList<ConnectionObserver>();
    }

    public synchronized void connectToServer(ConnectionObserver... observers) {
        endClientConnection();
        this.addObserver(observers);
        establishConnection();
    }

    private void establishConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteBuffer serverData = ByteBuffer.allocate(8);
                DatagramPacket serverPacket = new DatagramPacket(serverData.array(), 0, serverData.capacity());
                setConnectionState(ConnectionState.CONNECTING);
                try {
                    socket = new DatagramSocket();
                    socket.setSoTimeout(2000);
                    for (int i = 0; i < 5; i++) {
                        packageOutput(serverData, serverPacket);
                        try {
                            socket.send(serverPacket);
                            socket.receive(serverPacket);
                            if (Protocol.validRoomUpdate(serverData)) {
                                System.out.println("Client received room.");
                                setConnectionState(ConnectionState.CONNECTED);
                                handler = new ClientConnectionHandler(256, 1, 2000);
                                new ConnectionCenter(handler, socket, (InetSocketAddress) serverPacket.getSocketAddress());
                                return;
                            } else {
                                System.out.println("Invalid room response.");
                            }
                        } catch (SocketTimeoutException ste) {
                            System.out.println("Connection timed out, retrying");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                endClientConnection();
            }
        }).start();
    }

    private void packageOutput(ByteBuffer outputData, DatagramPacket outputPacket) {
        outputPacket.setAddress(serverAddress);
        outputPacket.setPort(SERVER_PORT);
        Protocol.packageRoomRequest(outputData);
    }

    private synchronized void addObserver(ConnectionObserver... observers) {
        this.connectionObservers.addAll(Arrays.asList(observers));
    }

    private synchronized void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
        for (ConnectionObserver observer : connectionObservers) {
            observer.connectionChange(connectionState);
        }
    }

    public synchronized void endClientConnection() {
        this.setConnectionState(ConnectionState.FAILED);
        if (socket != null)
            socket.close();
        System.out.println("Connection ended");
        this.connectionObservers.clear();
    }

    public static void main(String args[]) {
        new ConnectionClient().establishConnection();
    }
}
