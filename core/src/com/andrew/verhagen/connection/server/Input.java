package com.andrew.verhagen.connection.server;

public class Input {

    public static final byte RIGHT = 1;
    public static final byte LEFT = 2;
    public final byte inputType;
    public final int sequenceNumber;

    public Input(byte inputType, int sequenceNumber) {
        this.inputType = inputType;
        this.sequenceNumber = sequenceNumber;
    }
}
