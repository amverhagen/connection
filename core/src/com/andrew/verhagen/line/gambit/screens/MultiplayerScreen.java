package com.andrew.verhagen.line.gambit.screens;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.systems.graphics.ColorManagerSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.BlockMovementSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.BlockSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.GameRenderSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.InputTrackingSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.MovementPointSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.MultiplayerEntityFactory;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.MultiplayerTouchSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.MultiplayerWallSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.OpponentMovementSystem;
import com.andrew.verhagen.line.gambit.systems.multiplayerscreen.PlayerMovementSystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
    private InputProcessor inputProcessor;

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
            MultiplayerTouchSystem multiplayerTouchSystem = new MultiplayerTouchSystem(gameViewport, gameInstance);
            worldConfiguration.setSystem(multiplayerTouchSystem);
            this.inputProcessor = multiplayerTouchSystem;
            worldConfiguration.setSystem(new InputTrackingSystem(15));
            worldConfiguration.setSystem(PlayerMovementSystem.class);
            worldConfiguration.setSystem(OpponentMovementSystem.class);
            worldConfiguration.setSystem(MultiplayerWallSystem.class);
            worldConfiguration.setSystem(MovementPointSystem.class);
            worldConfiguration.setSystem(new BlockSystem(BlockSystem.SpawnLocation.RIGHT));

            //Update Systems
            worldConfiguration.setSystem(BlockMovementSystem.class);
            //Render Systems
            worldConfiguration.setSystem(new ColorManagerSystem(gameInstance));
            worldConfiguration.setSystem(new GameRenderSystem(gameInstance, gameCamera));
            multiplayerWorld = new World(worldConfiguration);
        }
    }

    @Override
    public void show() {
        loadWorld();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        multiplayerWorld.setDelta(delta);
        multiplayerWorld.process();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
