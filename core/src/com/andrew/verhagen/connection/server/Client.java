package com.andrew.verhagen.connection.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class Client {

    private DatagramPacket packet;
    private DatagramSocket socket;

    private InetAddress serverAddress;
    private int serverPort = 9001;

    private InetAddress roomAddress;
    private int roomPort;

    private InputThread inputThread;

    private byte connectionState = ConnectionStates.WAITING_FOR_OPPONENT;
    private int sequenceNumber;

    public Client() {
        try {
            serverAddress = InetAddress.getByName("192.168.0.4");
            socket = new DatagramSocket();
            packet = new DatagramPacket(new byte[4], 0, 4, serverAddress, serverPort);
            if (establishConnection()) {
                inputThread = new InputThread();
                inputThread.start();
                Client.OutputThread outputThread = this.new OutputThread();
                outputThread.start();
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

    private boolean setOutputData(ByteBuffer outputData) {
        outputData.clear();
        outputData.putInt(GameServer.PACKET_HEADER);
        if (connectionState == ConnectionStates.GAME_OVER) {
            return false;
        } else {
            outputData.put(connectionState);
            if (connectionState == ConnectionStates.WAITING_FOR_OPPONENT) {

            } else if (connectionState == ConnectionStates.GAME_ON) {
                outputData.putInt(sequenceNumber);
            }
            return true;
        }
    }

    private boolean isConnected() {
        if (connectionState == ConnectionStates.WAITING_FOR_OPPONENT
                || connectionState == ConnectionStates.GAME_ON
                || connectionState == ConnectionStates.STARTING)
            return true;
        else
            return false;
    }

    private class OutputThread extends OutputConnection {

        public OutputThread() {
            super();
        }

        @Override
        public void run() {
            try {
                while (isConnected() && setOutputData(outputBuffer)) {
                    System.out.println("Output position " + outputBuffer.position());
                    outputPacket.setLength(outputBuffer.position());
                    outputPacket.setAddress(roomAddress);
                    outputPacket.setPort(roomPort);
                    socket.send(outputPacket);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {

            }
            System.out.println("Stopped output");
        }
    }

    private class InputThread extends Thread {

        private ByteBuffer inputData;
        private DatagramPacket inputPacket;
        private float msSinceLastValidData;
        private final int timeOutTime = 2000;

        public InputThread() {
            inputData = ByteBuffer.allocate(256);
            inputPacket = new DatagramPacket(inputData.array(), inputData.capacity());
            msSinceLastValidData = 0;
        }

        @Override
        public void run() {
            try {
                socket.setSoTimeout(timeOutTime);
                while (isConnected()) {
                    socket.receive(inputPacket);
                    try {
                        //Check if incoming packet is from the established room
                        if (inputPacket.getAddress().equals(roomAddress) && inputPacket.getPort() == roomPort) {
                            readInputData();
                        } else handleInvalidData();
                    } catch (BufferUnderflowException underflow) {
                        handleInvalidData();
                    }
                    inputData.clear();
                }
            } catch (IOException e) {

            } finally {
                socket.close();
            }
            System.out.println("Stopped input");
        }

        private void readInputData() {
            inputData.limit(inputPacket.getLength());
            //Check if first four bytes match packet header
            if (inputData.getInt() == GameServer.PACKET_HEADER) {
                byte connectionState = inputData.get();
                if (connectionState == ConnectionStates.WAITING_FOR_OPPONENT) {
                    Client.this.connectionState = ConnectionStates.WAITING_FOR_OPPONENT;
                    System.out.format("Waiting for opponent\n");
                } else if (connectionState == ConnectionStates.GAME_ON) {
                    Client.this.connectionState = ConnectionStates.GAME_ON;
                    Client.this.sequenceNumber = inputData.getInt();
                }
            }
            msSinceLastValidData = 0;
        }

        private void handleInvalidData() {
            if (msSinceLastValidData >= timeOutTime) {
                connectionState = ConnectionStates.GAME_OVER;
            }
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
