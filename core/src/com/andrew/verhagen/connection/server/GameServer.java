package com.andrew.verhagen.connection.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class GameServer {

    public static final int PACKET_HEADER = 12312;
    private static final int MAX_ROOMS = 100;
    private DatagramSocket listeningSocket;
    private DatagramPacket inputPacket;
    private ArrayList<RoomConnection> roomConnections;

    public GameServer() {
        roomConnections = new ArrayList<RoomConnection>();
        inputPacket = new DatagramPacket(new byte[4], 4);
        try {
            listeningSocket = new DatagramSocket(9001);
            while (true) {
                boolean createNewRoom = true;
                System.out.println("Listening for game players");
                listeningSocket.receive(inputPacket);

                for (RoomConnection roomConnection : roomConnections) {
                    if (roomConnection.needsPlayer()) {
                        roomConnection.addPlayer(inputPacket.getAddress(), inputPacket.getPort());
                        createNewRoom = false;
                        break;
                    }
                }

                if (createNewRoom && roomConnections.size() != MAX_ROOMS) {
                    RoomConnection roomConnection = new RoomConnection();
                    roomConnection.addPlayer(inputPacket.getAddress(), inputPacket.getPort());
                    roomConnections.add(roomConnection);
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            listeningSocket.close();
        }
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
