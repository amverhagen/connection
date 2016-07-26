package com.andrew.verhagen.connection.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class RoomConnection {

    private OutputThread outputThread;
    private DatagramSocket socket;
    private Player playerOne;
    private Player playerTwo;
    private DatagramPacket outputPacket;
    private ByteBuffer outputByteBuffer;
    private int sequenceNumber;

    public RoomConnection() {
        outputThread = new OutputThread();
        playerOne = null;
        playerTwo = null;
        byte[] outputData = new byte[256];
        outputPacket = new DatagramPacket(outputData, outputData.length);
        outputByteBuffer = ByteBuffer.wrap(new byte[256]);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(InetAddress playerAddress, int playerPort) {
        System.out.println("Added player at " + playerAddress + " " + playerPort);
        if (playerOne == null) {
            playerOne = new Player(playerAddress, playerPort);
            outputThread.start();
        } else if (playerTwo == null) {
            playerTwo = new Player(playerAddress, playerPort);
        }
    }

    public boolean needsPlayer() {
        if (playerOne == null || playerTwo == null) return true;
        return false;
    }

    private void fillOutputPacketWithGameState() {
        outputByteBuffer.clear();
        outputByteBuffer.putInt(GameServer.PACKET_HEADER);
        if (this.needsPlayer()) {
            outputByteBuffer.put(ConnectionStates.WAITING_FOR_OPPONENT);
        } else {
            outputByteBuffer.put(ConnectionStates.GAME_ON);
            outputByteBuffer.putInt(sequenceNumber++);
        }
        outputPacket.setData(outputByteBuffer.array(), 0, outputByteBuffer.position());
    }

    private class OutputThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    fillOutputPacketWithGameState();
                    if (playerOne != null) {
                        outputPacket.setAddress(playerOne.address);
                        outputPacket.setPort(playerOne.port);
                        socket.send(outputPacket);
                    }
                    if (playerTwo != null) {
                        outputPacket.setAddress(playerTwo.address);
                        outputPacket.setPort(playerTwo.port);
                        socket.send(outputPacket);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } catch (IOException e) {

            } finally {
                socket.close();
            }
        }
    }

    private void writeGameState(GameState gameState) {
//        try {
////            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
////            outputStream.writeObject(gameState);
////            ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
////            channel.send(byteBuffer, playerOneAddress);
////            channel.send(byteBuffer, playerTwoAddress);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    protected class InputConnection implements Runnable {

        @Override
        public void run() {
            byte[] inputBuffer = new byte[256];
            ByteBuffer inputByteBuffer = ByteBuffer.wrap(inputBuffer);
            DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);
            InetAddress incomingAddress;
            while (true) {
                try {
                    socket.receive(inputPacket);
                    incomingAddress = inputPacket.getAddress();
//                    if (inputByteBuffer.getInt() == roomHeader) {
//                        if (incomingAddress == playerOneAddress) {
//
//                        } else if (incomingAddress == playerTwoAddress) {
//
//                        }
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
