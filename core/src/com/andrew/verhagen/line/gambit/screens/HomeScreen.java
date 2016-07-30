package com.andrew.verhagen.line.gambit.screens;

import com.andrew.verhagen.line.gambit.LineGambitGame;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.Screen;

public class HomeScreen implements Screen {

    private LineGambitGame gameInstance;
    private World homeWorld;

    public HomeScreen(LineGambitGame gambitGame) {
        this.gameInstance = gambitGame;
    }

    private void loadWorld() {
        if (homeWorld != null) {
            WorldConfiguration worldConfiguration = new WorldConfiguration();
            homeWorld = new World(worldConfiguration);
        }
    }

    @Override
    public void show() {
        loadWorld();
    }

    @Override
    public void render(float delta) {

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
