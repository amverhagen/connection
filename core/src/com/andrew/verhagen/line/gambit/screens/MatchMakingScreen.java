package com.andrew.verhagen.line.gambit.screens;

import com.andrew.verhagen.connection.client.ConnectionClient;
import com.andrew.verhagen.connection.room.ConnectionState;
import com.andrew.verhagen.line.gambit.GambitGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MatchMakingScreen implements Screen {

    private ConnectionClient connectionClient;
    private static final String SERVER_ADDRESS = "192.168.0.4";
    private static final int SERVER_PORT = 9001;

    private SpriteBatch batch;
    private final GambitGame gameInstance;
    private BitmapFont font;


    public MatchMakingScreen(GambitGame gambitGame) {
        this.gameInstance = gambitGame;
        this.batch = new SpriteBatch();
    }

    private void createConnectionClient() {
        try {
            this.connectionClient = new ConnectionClient(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
        } catch (UnknownHostException e) {
            if (connectionClient != null)
                connectionClient.endClientConnection();
        }
    }

    @Override
    public void show() {
        font = gameInstance.assets.manager.get("nixie48.ttf", BitmapFont.class);
        if (this.connectionClient == null)
            createConnectionClient();
    }

    @Override
    public void render(float delta) {
        if (connectionClient.getConnectionState() == ConnectionState.NOT_CONNECTED)
            connectionClient.connectToServer();
        draw(connectionClient.getConnectionState().stateDescription);
    }

    private void draw(String state) {
        batch.setProjectionMatrix(gameInstance.uiCamera.combined);
        batch.begin();
        font.draw(batch, state, gameInstance.UI_WIDTH * 0.05f, gameInstance.UI_HEIGHT * 0.10f);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        System.out.println("Paused called");
    }

    @Override
    public void resume() {
        System.out.println("Resume called");
    }

    @Override
    public void hide() {
        System.out.println("Hide called");
    }

    @Override
    public void dispose() {
        System.out.println("Dispose called");
        if (connectionClient != null)
            connectionClient.endClientConnection();
    }
}
