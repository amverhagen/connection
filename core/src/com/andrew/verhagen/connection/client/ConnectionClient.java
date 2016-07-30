package com.andrew.verhagen.connection.client;

import com.andrew.verhagen.connection.center.ConnectionCenter;
import com.andrew.verhagen.connection.server.GameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class ConnectionClient {

    private ConnectionCenter connectionCenter;
    private ClientConnectionHandler handler;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private ByteBuffer inputData;
    private InetAddress serverAddress;
    private int serverPort;
    public boolean connected;

    public ConnectionClient() {
        handler = new ClientConnectionHandler(256, 1, 2000);
        inputData = ByteBuffer.allocate(4);
        packet = new DatagramPacket(inputData.array(), 0, inputData.capacity());
        serverPort = 9001;
    }

    public void connectToServer() {
        try {
            connected = true;
            serverAddress = InetAddress.getByName("192.168.0.4");
            socket = new DatagramSocket();
            socket.setSoTimeout(2000);
            if (establishConnection()) {
                connectionCenter = new ConnectionCenter(handler, socket, (InetSocketAddress) packet.getSocketAddress());
            } else {
                System.out.println("Failed to connect to server");
                connected = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            socket.close();
        }
    }

    private boolean establishConnection() {
        boolean packetReceived = false;
        for (int i = 0; i < 10; i++) {
            try {
                packageOutput();
                socket.send(packet);
                socket.receive(packet);
                if (validInput()) {
                    packetReceived = true;
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return packetReceived;
    }

    private void packageOutput() {
        packet.setAddress(serverAddress);
        packet.setPort(serverPort);
        inputData.clear();
        inputData.putInt(GameServer.PACKET_HEADER);
    }

    private boolean validInput() throws BufferUnderflowException {
        inputData.limit(packet.getLength());
        inputData.position(0);
        boolean valid = inputData.getInt() == GameServer.PACKET_HEADER;
        inputData.clear();
        return valid;
    }

    public static void main(String[] args) {
        ConnectionClient cc = new ConnectionClient();
        cc.connectToServer();
    }
}
