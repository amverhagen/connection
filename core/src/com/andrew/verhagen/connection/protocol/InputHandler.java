package com.andrew.verhagen.connection.protocol;

import java.nio.ByteBuffer;

public interface InputHandler {

    boolean handleWaitingForOpponentInput(ByteBuffer inputBuffer, byte incomingState);

    boolean handleOpponentFoundInput(ByteBuffer inputBuffer, byte incomingState);

    boolean handleStartingInput(ByteBuffer inputBuffer, byte incomingState);

    boolean handleInputUpdateInput(ByteBuffer inputBuffer, byte incomingState);

    boolean handlePlayerOneWinInput(ByteBuffer inputBuffer, byte incomingState);

    boolean handlePlayerTwoWinInput(ByteBuffer inputBuffer, byte incomingState);

    boolean handleTieInput(ByteBuffer inputBuffer, byte incomingState);

    boolean handlesMessageType(byte inputState);
}
