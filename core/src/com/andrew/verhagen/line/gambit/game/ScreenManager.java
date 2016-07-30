package com.andrew.verhagen.line.gambit.game;

import com.andrew.verhagen.line.gambit.LineGambitGame;
import com.andrew.verhagen.line.gambit.screens.HomeScreen;

import java.util.ArrayList;

import com.andrew.verhagen.line.gambit.screens.LoadingScreen;
import com.andrew.verhagen.line.gambit.screens.PlayScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager {

    private final ArrayList<Screen> gameScreens;
    private final LineGambitGame gameInstance;

    public ScreenManager(LineGambitGame gameInstance) {
        this.gameScreens = new ArrayList<Screen>();
        this.gameInstance = gameInstance;
        this.initializeScreens();
    }

    public void initializeScreens() {
        gameScreens.add(new HomeScreen(gameInstance));
        gameScreens.add(new LoadingScreen(gameInstance));
        gameScreens.add(new PlayScreen(gameInstance));
    }

    public void setScreen(Class<? extends Screen> screen) {
        for (Screen gameScreen : gameScreens) {
            if (gameScreen.getClass().equals(screen)) {
                gameInstance.setScreen(gameScreen);
            }
        }
    }
}
