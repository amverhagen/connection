package com.andrew.verhagen.line.gambit.screens;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.systems.graphics.RenderSystem;
import com.andrew.verhagen.line.gambit.systems.graphics.ColorManagerSystem;
import com.andrew.verhagen.line.gambit.systems.homescreen.ColorPanelSystem;
import com.andrew.verhagen.line.gambit.systems.homescreen.HomeEntityFactory;
import com.andrew.verhagen.line.gambit.systems.homescreen.HomeScreenInitSystem;
import com.andrew.verhagen.line.gambit.systems.homescreen.HomeTouchSystem;
import com.andrew.verhagen.line.gambit.systems.movement.MoveToPointSystem;
import com.andrew.verhagen.line.gambit.systems.positional.RelativePositionSystem;
import com.artemis.Aspect;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class HomeScreen implements Screen {

    private GambitGame gameInstance;
    private World homeWorld;
    private HomeTouchSystem inputProcessor;

    public HomeScreen(GambitGame gambitGame) {
        this.gameInstance = gambitGame;
    }

    private void loadWorld() {
        if (homeWorld == null) {
            WorldConfiguration worldConfiguration = new WorldConfiguration();
            this.inputProcessor = new HomeTouchSystem(gameInstance.uiViewport, gameInstance);
            worldConfiguration.setSystem(inputProcessor);
            worldConfiguration.setSystem(HomeEntityFactory.class);
            worldConfiguration.setSystem(HomeScreenInitSystem.class);
            worldConfiguration.setSystem(new ColorManagerSystem(gameInstance));
            worldConfiguration.setSystem(ColorPanelSystem.class);
            worldConfiguration.setSystem(MoveToPointSystem.class);
            worldConfiguration.setSystem(RelativePositionSystem.class);
            worldConfiguration.setSystem(new RenderSystem(gameInstance, gameInstance.uiCamera, Aspect.all()));
            homeWorld = new World(worldConfiguration);
        }
    }

    @Override
    public void show() {
        loadWorld();
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        homeWorld.setDelta(delta);
        homeWorld.process();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resized");
    }

    @Override
    public void pause() {
        System.out.println("paused");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hidden");
    }

    @Override
    public void dispose() {
        if (homeWorld != null) {
            homeWorld.dispose();
            homeWorld = null;
        }
    }
}
