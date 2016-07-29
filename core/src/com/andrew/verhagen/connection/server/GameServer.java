package com.andrew.verhagen.connection.server;

import com.andrew.verhagen.connection.center.ConnectionCenter;
import com.andrew.verhagen.connection.room.RoomConnectionHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class GameServer {

    public static final int PACKET_HEADER = 12312;
    private static final int MAX_ROOMS = 100;
    private DatagramSocket listeningSocket;
    private DatagramPacket inputPacket;
    private ByteBuffer inputData;
    private ArrayList<ConnectionCenter> connectionCenters;

    public GameServer() {
        connectionCenters = new ArrayList<ConnectionCenter>();
        inputData = ByteBuffer.allocate(4);
        inputPacket = new DatagramPacket(inputData.array(), inputData.capacity());
        try {
            listeningSocket = new DatagramSocket(9001);
            findConnection:
            while (true) {
                System.out.println("Listening for game players.");
                listeningSocket.receive(inputPacket);
                System.out.println("Package received.");

                if (!validInput())
                    continue findConnection;
                System.out.println("Package has valid input.");

                InetSocketAddress incomingAddress = (InetSocketAddress) inputPacket.getSocketAddress();

                for (ConnectionCenter connectionCenter : connectionCenters) {
                    if (connectionCenter.holdingConnection(incomingAddress))
                        continue findConnection;
                }
                System.out.println("Package is not already held.");

                for (ConnectionCenter connectionCenter : connectionCenters) {
                    if (connectionCenter.addAddress(incomingAddress))
                        continue findConnection;
                }
                System.out.println("No open rooms. Creating new room.");

                if (connectionCenters.size() < MAX_ROOMS) {
                    ConnectionCenter connectionCenter = new ConnectionCenter(new RoomConnectionHandler(256, 2, 2000));
                    connectionCenters.add(connectionCenter);
                    connectionCenter.addAddress(incomingAddress);
                    System.out.println("Created new room");
                }

            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            listeningSocket.close();
        }
    }

    private boolean validInput() throws BufferUnderflowException {
        inputData.limit(inputPacket.getLength());
        inputData.position(0);
        boolean valid = inputData.getInt() == GameServer.PACKET_HEADER;
        inputData.clear();
        return valid;
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
