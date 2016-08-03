package com.andrew.verhagen.line.gambit.network;

import java.nio.ByteBuffer;

public abstract class ProtocolFunction {

    int numberOfBytes;

    public ProtocolFunction(int numberOfBytes) {
        this.numberOfBytes = numberOfBytes;
    }

    abstract void handleBuffer(ByteBuffer buffer);
}
