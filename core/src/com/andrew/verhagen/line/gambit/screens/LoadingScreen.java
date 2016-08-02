package com.andrew.verhagen.line.gambit.screens;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoadingScreen implements Screen {

    private SpriteBatch batch;
    private final GambitGame gameInstance;
    private BitmapFont font;

    public LoadingScreen(GambitGame gambitGame) {
        this.gameInstance = gambitGame;
        this.batch = new SpriteBatch();
    }

    @Override
    public void show() {
        font = gameInstance.assets.manager.get("nixie48.ttf", BitmapFont.class);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(gameInstance.uiCamera.combined);
        batch.begin();
        font.draw(batch, "Loading . . .", gameInstance.UI_WIDTH * 0.80f, gameInstance.UI_HEIGHT * 0.10f);
        batch.end();
        gameInstance.assets.manager.finishLoading();
        gameInstance.screenManager.setScreen(HomeScreen.class);
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

    }
}
