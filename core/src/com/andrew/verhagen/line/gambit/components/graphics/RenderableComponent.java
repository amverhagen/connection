package com.andrew.verhagen.line.gambit.components.graphics;

import com.badlogic.gdx.graphics.Texture;

public class RenderableComponent extends com.artemis.PooledComponent {
    public transient Texture texture;
    public String textureName;
    public float a = 1f;
    public float r = 1f;
    public float g = 1f;
    public float b = 1f;

    @Override
    protected void reset() {
        texture = null;
        textureName = null;
        a = 1f;
        r = 1f;
        g = 1f;
        b = 1f;
    }
}
