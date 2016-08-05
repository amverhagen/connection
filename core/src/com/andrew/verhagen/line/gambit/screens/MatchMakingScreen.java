package com.andrew.verhagen.line.gambit.screens;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.systems.graphics.ColorManagerSystem;
import com.andrew.verhagen.line.gambit.systems.graphics.RenderSystem;
import com.andrew.verhagen.line.gambit.systems.matchmaking.ConnectionOutputStatusSystem;
import com.andrew.verhagen.line.gambit.systems.matchmaking.ConnectionSystem;
import com.andrew.verhagen.line.gambit.systems.matchmaking.MatchMakingEntityFactory;
import com.andrew.verhagen.line.gambit.systems.matchmaking.MatchMakingTouchSystem;
import com.artemis.Aspect;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;


public class MatchMakingScreen implements Screen {

    private World matchMakingWorld;
    private final GambitGame gameInstance;
    private ConnectionSystem connectionSystem;
    private InputProcessor inputProcessor;

    public MatchMakingScreen(GambitGame gambitGame) {
        this.gameInstance = gambitGame;
        this.connectionSystem = new ConnectionSystem();
    }

    private void initiateWorld() {
        if (matchMakingWorld == null) {
            WorldConfiguration worldConfiguration = new WorldConfiguration();
            worldConfiguration.setSystem(MatchMakingEntityFactory.class);
            MatchMakingTouchSystem touchSystem = new MatchMakingTouchSystem(gameInstance.uiViewport);
            this.inputProcessor = touchSystem;
            worldConfiguration.setSystem(touchSystem);
            worldConfiguration.setSystem(connectionSystem = new ConnectionSystem());
            worldConfiguration.setSystem(new ConnectionOutputStatusSystem(gameInstance, gameInstance.uiCamera));
            worldConfiguration.setSystem(new ColorManagerSystem(gameInstance));
            worldConfiguration.setSystem(new RenderSystem(gameInstance, gameInstance.uiCamera, Aspect.all()));
            matchMakingWorld = new World(worldConfiguration);
        }
    }

    @Override
    public void show() {
        initiateWorld();
        Gdx.input.setInputProcessor(this.inputProcessor);
    }

    @Override
    public void render(float delta) {
        matchMakingWorld.setDelta(delta);
        matchMakingWorld.process();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        System.out.println("Paused called");
        this.endConnection();
    }

    @Override
    public void resume() {
        System.out.println("Resume called");
    }

    @Override
    public void hide() {
        System.out.println("Hide called");
        this.endConnection();
    }

    @Override
    public void dispose() {
        System.out.println("Dispose called");
        this.endConnection();
    }

    private void endConnection() {
        if (connectionSystem != null)
            connectionSystem.endConnection();
    }
}
