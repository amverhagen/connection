package com.andrew.verhagen.connection.center;

import java.net.DatagramPacket;

public interface UDPInputHandler {

    boolean handleInput(DatagramPacket incomingPacket);
}
