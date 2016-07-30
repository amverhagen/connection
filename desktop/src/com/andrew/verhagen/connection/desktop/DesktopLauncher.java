package com.andrew.verhagen.connection.desktop;

import com.andrew.verhagen.line.gambit.LineGambitGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 540;
        config.width = 960;
        new LwjglApplication(new LineGambitGame(), config);
    }
}
