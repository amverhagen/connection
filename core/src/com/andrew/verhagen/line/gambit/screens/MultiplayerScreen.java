package com.andrew.verhagen.line.gambit.screens;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.systems.graphics.ColorManagerSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.BlockSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.GameRenderSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.MultiplayerEntityFactory;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.MultiplayerWallSystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MultiplayerScreen implements Screen {

    public static final float GAME_WORLD_HEIGHT = 1080;
    public static final float GAME_WORLD_WIDTH = 1920;

    public final OrthographicCamera gameCamera;
    public final FitViewport gameViewport;

    private World multiplayerWorld;
    private GambitGame gameInstance;

    public MultiplayerScreen(GambitGame gameInstance) {
        this.gameInstance = gameInstance;
        this.gameCamera = new OrthographicCamera();
        this.gameViewport = new FitViewport(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT, this.gameCamera);
        this.gameViewport.apply(true);
    }

    private void loadWorld() {
        if (multiplayerWorld == null) {
            WorldConfiguration worldConfiguration = new WorldConfiguration();
            worldConfiguration.setSystem(MultiplayerEntityFactory.class);
            worldConfiguration.setSystem(MultiplayerWallSystem.class);
            worldConfiguration.setSystem(new BlockSystem(BlockSystem.SpawnLocation.LEFT));

            //Render systems
            worldConfiguration.setSystem(new ColorManagerSystem(gameInstance));
            worldConfiguration.setSystem(new GameRenderSystem(gameInstance, gameCamera));
            multiplayerWorld = new World(worldConfiguration);
        }
    }

    @Override
    public void show() {
        loadWorld();
        Gdx.app.log("MultiplayerScreen", "shown.");
    }

    @Override
    public void render(float delta) {
        multiplayerWorld.setDelta(delta);
        multiplayerWorld.process();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (multiplayerWorld != null)
            multiplayerWorld.dispose();
    }
}
