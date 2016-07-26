package com.andrew.verhagen.connection.server;

import java.net.InetAddress;

public class Player {
    InetAddress address;
    int port;
    long timeSinceLastInput;
    boolean connected;

    public Player() {
        this.address = null;
        this.port = 0;
        this.connected = false;
    }

    public Player(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.connected = true;
    }
}
