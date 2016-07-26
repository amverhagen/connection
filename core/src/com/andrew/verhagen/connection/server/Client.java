package com.andrew.verhagen.connection.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {

    private DatagramPacket packet;
    private DatagramSocket socket;
    private DatagramChannel channel;

    private InetAddress serverAddress;
    private int serverPort = 9001;

    private InetAddress roomAddress;
    private int roomPort;

    private InputThread inputThread;

    public Client() {
        try {
            serverAddress = InetAddress.getByName("192.168.0.4");
            socket = new DatagramSocket();
            packet = new DatagramPacket(new byte[4], 0, 4, serverAddress, serverPort);
            channel = DatagramChannel.open();
            channel.socket().bind(null);
            if (establishConnection()) {
                inputThread = new InputThread();
                inputThread.start();
            } else {
                System.out.println("Failed to connect to server");
            }
        } catch (IOException e) {

        } finally {

        }
    }

    private boolean establishConnection() {
        for (int i = 0; i < 10; i++) {
            try {
                socket.setSoTimeout(1000);
                socket.send(packet);
                socket.receive(packet);
                roomAddress = packet.getAddress();
                roomPort = packet.getPort();
                System.out.println("Connected to Server");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private class InputThread extends Thread {

        @Override
        public void run() {
            byte[] inputArray = new byte[256];
            ByteBuffer inputData = ByteBuffer.wrap(inputArray);
            DatagramPacket inputPacket = new DatagramPacket(inputArray, inputArray.length);
            try {
                socket.setSoTimeout(10000);
                while (true) {
                    System.out.println("Waiting for input");
                    socket.receive(inputPacket);
                    inputData.limit(inputPacket.getLength());

                    if (inputPacket.getAddress().equals(roomAddress) && inputPacket.getPort() == roomPort) {

                        if (inputData.getInt() == GameServer.PACKET_HEADER) {
                            byte connectionState = inputData.get();
                            if (connectionState == ConnectionStates.WAITING_FOR_OPPONENT) {
                                System.out.println("Waiting for opponent");
                            } else if (connectionState == ConnectionStates.GAME_ON) {
                                int sequenceNumber = inputData.getInt();
                                System.out.format("Game on sn:%d\n", sequenceNumber);
                            }
                        }
                    }
                    inputData.clear();
                }
            } catch (IOException e) {

            } finally {
                socket.close();
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
