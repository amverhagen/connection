package com.andrew.verhagen.connection.protocol;

public enum ConnectionState {
    NOT_CONNECTED("Not Connected"),
    CONNECTING("Connecting to Server"),
    CONNECTED("Connected to Server"),
    FAILED("Unable to Connect to Server"),
    WAITING_FOR_OPPONENT("Waiting for Opponent"),
    GAME_ON("Game On.");

    public final String stateDescription;

    ConnectionState(String stateDescription) {
        this.stateDescription = stateDescription;
    }

}

