package com.andrew.verhagen.line.gambit.systems.matchmaking;

import com.andrew.verhagen.connection.room.ConnectionState;
import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ConnectionOutputStatusSystem extends BaseSystem implements ConnectionObserver {

    private ConnectionSystem connectionSystem;
    private ConnectionState currentConnectionState;
    private SpriteBatch batch;
    private GambitGame gameInstance;
    private Camera camera;
    private BitmapFont font;
    private int currentDot = 0;
    private boolean addDots;
    private int nextDot;
    private float dotTime;
    private String[] dots = {"", " .", " . .", " . . .", " . . . ."};
    private String connectionDescription;
    private String outputString;

    public ConnectionOutputStatusSystem(GambitGame gameInstance, Camera camera) {
        this.gameInstance = gameInstance;
        this.camera = camera;
        this.batch = new SpriteBatch();
    }

    @Override
    protected void initialize() {
        font = gameInstance.assets.manager.get(Assets.nixie48, BitmapFont.class);
        font.setColor(gameInstance.gameColor);
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
    }

    @Override
    protected void processSystem() {
        if (this.addDots) {
            if (updateDot(world.getDelta()))
                outputString = connectionDescription + dots[currentDot];
        } else
            outputString = connectionDescription;

        font.draw(batch, outputString, GambitGame.UI_WIDTH * 0.05f, GambitGame.UI_HEIGHT * 0.10f);
    }

    @Override
    protected void end() {
        batch.end();
    }

    private boolean updateDot(float delta) {
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
        this.connectionDescription = newState.stateDescription;
        if (newState == ConnectionState.CONNECTING)
            this.addDots = true;
    }
}
