package com.andrew.verhagen.connection;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ConnectionGame extends ApplicationAdapter {
    ConnectionThread connectionThread;
    SpriteBatch batch;
    Texture img;
    float spritePosition;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        connectionThread = new ConnectionThread();
        connectionThread.start();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spritePosition = connectionThread.position;
        batch.begin();
        batch.draw(img, spritePosition, spritePosition);
        batch.end();
    }
}
