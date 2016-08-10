package com.andrew.verhagen.connection.protocol;

import java.nio.ByteBuffer;

public class OutputHandlerMapper {

    private final OutputHandler handler;

    public OutputHandlerMapper(OutputHandler handler) {
        this.handler = handler;
    }

    public boolean packOutput(ByteBuffer outputBuffer) {
        synchronized (handler) {
            byte currentOutputState = handler.getOutputState();
            Protocol.packMessage(outputBuffer, currentOutputState);
            switch (currentOutputState) {
                case Protocol.WAITING_FOR_OPPONENT:
                    return handler.packWaitingForOpponentOutput(outputBuffer);
                case Protocol.OPPONENT_FOUND:
                    return handler.packOpponentFoundOutput(outputBuffer);
                case Protocol.STARTING:
                    return handler.packGameStartingOutput(outputBuffer);
                case Protocol.INPUT_UPDATE:
                    return handler.packInputUpdateOutput(outputBuffer);
                case Protocol.PLAYER_ONE_WIN:
                    return handler.packPlayerOneWinOutput(outputBuffer);
                case Protocol.PLAYER_TWO_WIN:
                    return handler.packPlayerTwoWinOutput(outputBuffer);
                case Protocol.TIE:
                    return handler.packTieOutput(outputBuffer);
                default:
                    return false;
            }
        }
    }
}
