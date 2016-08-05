package com.andrew.verhagen.line.gambit.systems.matchmaking;

import com.andrew.verhagen.connection.room.ConnectionState;
import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.home.ManagedColor;
import com.andrew.verhagen.line.gambit.components.input.Touchable;
import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.andrew.verhagen.line.gambit.systems.graphics.RenderSystem;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ConnectionOutputStatusSystem extends BaseSystem implements ConnectionObserver {

    private ConnectionState currentConnectionState;
    private RenderSystem renderSystem;
    private ComponentMapper<Touchable> touchableComponentMapper;
    private ComponentMapper<ManagedColor> managedColorComponentMapper;
    private MatchMakingEntityFactory entityFactory;
    private SpriteBatch batch;
    private GambitGame gameInstance;
    private Camera camera;
    private BitmapFont font;
    private int currentDot = -1;
    private int nextDot;
    private float dotTime;
    private int connectionButton;
    private Touchable connectionButtonTouchable;
    private String[] dots = {"", " .", " . .", " . . .", " . . . ."};
    private String outputString;

    public ConnectionOutputStatusSystem(GambitGame gameInstance, Camera camera) {
        this.gameInstance = gameInstance;
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.outputString = new String();
        this.currentConnectionState = ConnectionState.NOT_CONNECTED;
    }

    @Override
    protected void initialize() {
        font = gameInstance.assets.manager.get(Assets.nixie48, BitmapFont.class);
        font.setColor(gameInstance.gameColor);
        connectionButton = entityFactory.createButton(95f, 175f, 100f, 100f, Assets.clear, null);
        managedColorComponentMapper.create(connectionButton);
        connectionButtonTouchable = touchableComponentMapper.get(connectionButton);
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void processSystem() {
        updateOutput(this.currentConnectionState, world.getDelta());
        font.draw(batch, outputString, GambitGame.UI_WIDTH * 0.05f, GambitGame.UI_HEIGHT * 0.10f);
    }

    @Override
    protected void end() {
        batch.end();
    }

    private synchronized void updateOutput(ConnectionState connectionState, float delta) {
        if (this.currentConnectionState != connectionState)
            changeOutput(connectionState);
        if (this.currentConnectionState == ConnectionState.CONNECTING) {
            if (updateDot(delta))
                this.outputString = currentConnectionState.stateDescription + dots[currentDot];
        }
    }

    private synchronized void changeOutput(ConnectionState newState) {
        if (newState == ConnectionState.CONNECTING) {
            this.setButtonToCancel();
            this.dotTime = 0;
            this.nextDot = -1;
        } else {
            this.setButtonToRetry();
        }
        this.currentConnectionState = newState;
        this.outputString = currentConnectionState.stateDescription;
    }

    private synchronized boolean updateDot(float delta) {
        this.dotTime += delta;
        this.nextDot = ((int) dotTime) % dots.length;
        if (currentDot != nextDot) {
            this.currentDot = nextDot;
            return true;
        }
        return false;
    }

    @Override
    public void connectionChange(ConnectionState newState) {
        this.updateOutput(newState, 0f);
    }

    private void setButtonToCancel() {
        connectionButtonTouchable.touchEventName = MatchMakingTouchSystem.cancelConnection;
        renderSystem.setRenderableTexture(connectionButton, Assets.musicIcon);
    }

    private void setButtonToRetry() {
        connectionButtonTouchable.touchEventName = MatchMakingTouchSystem.retryConnection;
        renderSystem.setRenderableTexture(connectionButton, Assets.retry);
    }
}
