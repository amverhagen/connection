package com.andrew.verhagen.line.gambit.systems.matchmaking;

import com.andrew.verhagen.connection.protocol.ConnectionState;

public interface ConnectionObserver {

    void connectionChange(ConnectionState newState);
}
