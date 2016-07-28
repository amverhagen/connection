package com.andrew.verhagen.connection.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class RoomConnection {

    private RoomOutputConnection outputThread;
    private RoomInputConnection inputThread;
    private DatagramSocket socket;
    private Player playerOne;
    private Player playerTwo;
    private boolean roomNeedsPlayer;
    private int sequenceNumber;

    public RoomConnection() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        outputThread = new RoomOutputConnection();
        inputThread = new RoomInputConnection(socket);
        clearRoom();
    }

    private void clearRoom() {
        playerOne = new Player();
        playerTwo = new Player();
        roomNeedsPlayer = true;
    }

    public void addPlayer(InetAddress playerAddress, int playerPort) {
        System.out.println("Added player at " + playerAddress + " " + playerPort);
        if (!playerOne.connected) {
            playerOne = new Player(playerAddress, playerPort);
            playerOne.timeSinceLastInput = System.nanoTime();
            outputThread.start();
            inputThread.start();
        } else if (!playerTwo.connected) {
            playerTwo = new Player(playerAddress, playerPort);
            playerTwo.timeSinceLastInput = System.nanoTime();
            roomNeedsPlayer = false;
        }
    }

    public boolean needsPlayer() {
        return roomNeedsPlayer;
    }

    private void fillByteBufferWithGameState(ByteBuffer buffer) {
        buffer.clear();
        buffer.putInt(GameServer.PACKET_HEADER);
        if (this.needsPlayer()) {
            buffer.put(ConnectionStates.WAITING_FOR_OPPONENT);
        } else {
            buffer.put(ConnectionStates.GAME_ON);
            buffer.putInt(sequenceNumber++);
        }
    }

    private Player getPlayerWithAddress(InetAddress incomingAddress, int incomingPort) {
        if (incomingAddress.equals(playerOne.address) && incomingPort == playerOne.port) {
            return playerOne;
        } else if (incomingAddress.equals(playerTwo.address) && incomingPort == playerTwo.port) {
            return playerTwo;
        } else return null;
    }

    private class RoomOutputConnection extends OutputConnection {

        public RoomOutputConnection() {
            super();
        }

        @Override
        public void run() {
            try {
                while (playerOne.connected || playerTwo.connected) {
                    fillByteBufferWithGameState(outputBuffer);
                    outputPacket.setLength(outputBuffer.position());
                    if (playerOne.connected) {
                        outputPacket.setAddress(playerOne.address);
                        outputPacket.setPort(playerOne.port);
                        socket.send(outputPacket);
                    }
                    if (playerTwo.connected) {
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
            System.out.print("Room output stopped");
        }
    }

    protected class RoomInputConnection extends InputConnection {

        private Player currentInputPlayer;

        public RoomInputConnection(DatagramSocket socket) {
            super(socket, 2000000000l);
        }

        @Override
        protected boolean runCondition() {
            return playerOne.connected || playerTwo.connected;
        }

        @Override
        protected void handleInputData() {
            long receptionTime = System.nanoTime();

            currentInputPlayer = getPlayerWithAddress(inputPacket.getAddress(), inputPacket.getPort());

            if (currentInputPlayer != null) {
                if (inputData.getInt() == GameServer.PACKET_HEADER) {
                    if (inputData.get() == ConnectionStates.WAITING_FOR_OPPONENT) {

                    } else if (inputData.get() == ConnectionStates.GAME_ON) {

                    }
                    currentInputPlayer.timeSinceLastInput = receptionTime;
                }
            }
            if (playerOne.connected) {
                if ((receptionTime - playerOne.timeSinceLastInput) > timeOutTimeInNanoSeconds)
                    playerOne.connected = false;
            }
            if (playerTwo.connected) {
                if ((receptionTime - playerTwo.timeSinceLastInput) > timeOutTimeInNanoSeconds)
                    playerTwo.connected = false;
            }
        }

        @Override
        protected void handleFinally() {
            clearRoom();
        }
    }
}
