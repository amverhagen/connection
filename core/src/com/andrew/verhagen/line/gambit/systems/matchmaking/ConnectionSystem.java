package com.andrew.verhagen.line.gambit.systems.matchmaking;

import com.andrew.verhagen.connection.client.ConnectionClient;
import com.andrew.verhagen.connection.room.ConnectionState;
import com.andrew.verhagen.line.gambit.components.input.Touchable;
import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.andrew.verhagen.line.gambit.systems.graphics.RenderSystem;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;

public class ConnectionSystem extends BaseSystem {

    private RenderSystem renderSystem;
    private ConnectionOutputStatusSystem statusSystem;
    private ComponentMapper<Touchable> touchableComponentMapper;
    private MatchMakingEntityFactory entityFactory;
    private ConnectionClient connectionClient;
    private int connectionButton;

    public ConnectionSystem() {
        this.connectionClient = new ConnectionClient();
    }

    @Override
    protected void initialize() {
        connectionButton = entityFactory.createButton(95f, 175f, 100f, 100f, Assets.clear, null);
        this.connectionClient.addObserver(statusSystem);
    }

    @Override
    protected void processSystem() {
        if (connectionClient.getConnectionState() == ConnectionState.FAILED) {

        }
        retryConnection();
    }

    private void setButtonToCancel() {
        renderSystem.setRenderableTexture(connectionButton, Assets.musicIcon);
        touchableComponentMapper.get(connectionButton).touchEventName = MatchMakingTouchSystem.cancelConnection;
    }

    private void setButtonToRetry() {
        renderSystem.setRenderableTexture(connectionButton, Assets.retry);
        touchableComponentMapper.get(connectionButton).touchEventName = MatchMakingTouchSystem.retryConnection;
    }

    public void retryConnection() {
        connectionClient.connectToServer();
    }

    public void endConnection() {
        connectionClient.endClientConnection();
        this.setEnabled(false);
    }
}
