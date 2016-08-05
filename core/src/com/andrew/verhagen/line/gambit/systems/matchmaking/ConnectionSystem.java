package com.andrew.verhagen.line.gambit.systems.matchmaking;

import com.andrew.verhagen.connection.client.ConnectionClient;
import com.artemis.BaseSystem;

public class ConnectionSystem extends BaseSystem {

    private ConnectionOutputStatusSystem statusSystem;
    private ConnectionClient connectionClient;

    public ConnectionSystem() {
        this.connectionClient = new ConnectionClient();
    }

    @Override
    protected void processSystem() {
        System.out.println("Retry called");
        retryConnection();
    }

    @Override
    protected void end() {
        this.setEnabled(false);
    }

    public void retryConnection() {
        connectionClient.connectToServer(statusSystem);
    }

    public void endConnection() {
        connectionClient.endClientConnection();
        this.setEnabled(false);
    }
}
