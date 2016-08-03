package com.andrew.verhagen.line.gambit.network;

import com.andrew.verhagen.connection.server.GameServer;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Protocol {
    HashMap<Byte, ProtocolFunction> protocolFunctionHashMap;
    public final byte holdsServerInput = (byte) 1;
    public final byte holdsClientInput = (byte) 2;

    public Protocol() {
        this.protocolFunctionHashMap = new HashMap<Byte, ProtocolFunction>();

        ProtocolFunction serverInput = new ProtocolFunction(30) {
            @Override
            void handleBuffer(ByteBuffer buffer) {
                return;
            }
        };
        protocolFunctionHashMap.put(holdsServerInput, serverInput);
    }

    public void sendData(byte protocol, ByteBuffer data) {

    }

    public static boolean validInput(ByteBuffer data) throws BufferUnderflowException {
        data.position(0);
        return data.getInt() == GameServer.PACKET_HEADER;
    }

}
