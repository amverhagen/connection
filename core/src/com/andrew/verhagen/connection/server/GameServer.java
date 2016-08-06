package com.andrew.verhagen.connection.server;

import com.andrew.verhagen.connection.center.ConnectionCenter;
import com.andrew.verhagen.connection.protocol.Protocol;
import com.andrew.verhagen.connection.room.RoomConnectionHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class GameServer {

    private static final int MAX_ROOMS = 100;
    private DatagramSocket listeningSocket;
    private DatagramPacket inputPacket;
    private ByteBuffer inputData;
    private ArrayList<ConnectionCenter> connectionCenters;
    private String tag = "GameSever: ";

    public GameServer() {
        inputData = ByteBuffer.allocate(8);
        inputPacket = new DatagramPacket(inputData.array(), inputData.capacity());
        connectionCenters = new ArrayList<ConnectionCenter>();
    }

    public void startServer() {
        while (true) {
            resetCenters();
            openServerSocket();
            listenForConnections();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetCenters() {
        for (ConnectionCenter center : connectionCenters) {
            center.closeCenter();
        }
        connectionCenters.clear();
    }

    private boolean openServerSocket() {
        try {
            listeningSocket = new DatagramSocket(9001);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            if (listeningSocket != null)
                listeningSocket.close();
        }
        return false;
    }

    private void listenForConnections() {
        try {
            findConnection:
            while (true) {
                System.out.println(tag + "Listening for game players.");
                listeningSocket.receive(inputPacket);
                System.out.println(tag + "Package received.");

                if (!Protocol.validRoomRequest(inputData))
                    continue;
                System.out.println(tag + "Package has valid input.");

                InetSocketAddress incomingAddress = (InetSocketAddress) inputPacket.getSocketAddress();

                for (ConnectionCenter connectionCenter : connectionCenters) {
                    if (connectionCenter.holdingConnection(incomingAddress)) {
                        System.out.println(tag + "Connection already held.");
                        continue findConnection;
                    }
                }
                System.out.println(tag + "Package is not already held.");

                for (ConnectionCenter connectionCenter : connectionCenters) {
                    if (connectionCenter.addAddress(incomingAddress)) {
                        System.out.println(tag + "Added address " + incomingAddress + " to room");
                        continue findConnection;
                    }
                }
                System.out.println(tag + "No open rooms. Creating new room.");

                if (connectionCenters.size() < MAX_ROOMS) {
                    ConnectionCenter connectionCenter = new ConnectionCenter(new RoomConnectionHandler(256, 2, 2000));
                    connectionCenters.add(connectionCenter);
                    connectionCenter.addAddress(incomingAddress);
                    System.out.println(tag + "Created new room");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(tag + "Error while listening for connections.");
        }
    }

    public static void main(String[] args) {
        new GameServer().startServer();
    }
}
