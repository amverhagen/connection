package com.andrew.verhagen.connection.protocol;

import java.nio.ByteBuffer;

public interface OutputHandler {

    boolean packWaitingForOpponentOutput(ByteBuffer outputBuffer);

    boolean packOpponentFoundOutput(ByteBuffer inputBuffer);

    boolean packGameStartingOutput(ByteBuffer inputBuffer);

    boolean packInputUpdateOutput(ByteBuffer inputBuffer);

    boolean packPlayerOneWinOutput(ByteBuffer inputBuffer);

    boolean packPlayerTwoWinOutput(ByteBuffer inputBuffer);

    boolean packTieOutput(ByteBuffer inputBuffer);

    byte getOutputState();
}
