package com.andrew.verhagen.line.gambit.systems.matchmaking;

import com.andrew.verhagen.connection.room.ConnectionState;

public interface ConnectionObserver {

    public void connectionChange(ConnectionState newState);
}
