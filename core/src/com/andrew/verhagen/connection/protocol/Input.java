package com.andrew.verhagen.connection.protocol;

public class Input {

    public static final byte NOT_RECEIVED = -1;
    public static final byte NO_INPUT = 0;
    public static final byte RIGHT = 1;
    public static final byte LEFT = 2;
    private static final byte[] VALID_INPUTS = {NOT_RECEIVED, NO_INPUT, RIGHT, LEFT};

    public static final boolean isValidInput(byte inputValue) {
        for (byte validInput : VALID_INPUTS) {
            if (validInput == inputValue)
                return true;
        }
        return false;
    }

    public static final boolean isValidInput(byte[] incomingValues) {
        for (byte incomingInput : incomingValues) {
            if (!isValidInput(incomingInput))
                return false;
        }
        return true;
    }
}
