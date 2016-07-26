package com.andrew.verhagen.connection.server;

import java.net.InetAddress;

public class Player {
    InetAddress address;
    int port;
    float timeSinceLastInput;

    public Player(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        float timeSinceLastInput = 0;
    }
}
