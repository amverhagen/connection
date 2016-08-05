package com.andrew.verhagen.line.gambit;

import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.andrew.verhagen.line.gambit.gameutils.GameColors;
import com.andrew.verhagen.line.gambit.gameutils.ScreenManager;
import com.andrew.verhagen.line.gambit.screens.LoadingScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GambitGame extends Game {

    public static final int UI_WIDTH = 1920;
    public static final int UI_HEIGHT = 1080;

    public Assets assets;
    public Camera uiCamera;
    public Viewport uiViewport;
    public ScreenManager screenManager;
    public Color gameColor;

    @Override
    public void create() {
        System.out.println("Create called");
        gameColor = new Color(GameColors.WHITE);
        assets = new Assets();
        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(UI_WIDTH, UI_HEIGHT, uiCamera);
        uiViewport.apply(true);
        screenManager = new ScreenManager(this);
        screenManager.setScreen(LoadingScreen.class);
    }

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height);
        super.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        screenManager.dispose();
        assets.manager.dispose();
    }

    public void setGameColor(float r, float g, float b) {
        gameColor.r = r;
        gameColor.g = g;
        gameColor.b = b;
    }
}
