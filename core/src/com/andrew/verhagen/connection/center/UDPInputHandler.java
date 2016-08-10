package com.andrew.verhagen.connection.center;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface UDPInputHandler {

    DatagramPacket getDatagramPacket();

    DatagramSocket getInputSocket();

    boolean handleInput();
}
