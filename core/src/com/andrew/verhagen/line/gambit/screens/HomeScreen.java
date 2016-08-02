package com.andrew.verhagen.line.gambit.screens;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.systems.homescreen.ColorManagerSystem;
import com.andrew.verhagen.line.gambit.systems.homescreen.HomeEntityFactory;
import com.andrew.verhagen.line.gambit.systems.graphics.RenderSystem;
import com.andrew.verhagen.line.gambit.systems.homescreen.ColorPanelSystem;
import com.andrew.verhagen.line.gambit.systems.homescreen.HomeScreenInitSystem;
import com.andrew.verhagen.line.gambit.systems.homescreen.HomeTouchSystem;
import com.andrew.verhagen.line.gambit.systems.movement.MoveToPointSystem;
import com.andrew.verhagen.line.gambit.systems.positional.RelativePositionSystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
            this.inputProcessor = new HomeTouchSystem(gameInstance.uiViewport);
            worldConfiguration.setSystem(inputProcessor);
            worldConfiguration.setSystem(HomeEntityFactory.class);
            worldConfiguration.setSystem(HomeScreenInitSystem.class);
            worldConfiguration.setSystem(ColorManagerSystem.class);
            worldConfiguration.setSystem(ColorPanelSystem.class);
            worldConfiguration.setSystem(MoveToPointSystem.class);
            worldConfiguration.setSystem(RelativePositionSystem.class);
            worldConfiguration.setSystem(new RenderSystem(gameInstance));
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
        if (homeWorld != null) {
            homeWorld.dispose();
            homeWorld = null;
        }
    }
}
