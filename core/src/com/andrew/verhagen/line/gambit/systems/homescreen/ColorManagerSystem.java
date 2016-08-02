package com.andrew.verhagen.line.gambit.systems.homescreen;

import com.andrew.verhagen.line.gambit.components.graphics.Renderable;
import com.andrew.verhagen.line.gambit.components.home.ManagedColor;
import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class ColorManagerSystem extends IteratingSystem {
    private ComponentMapper<Renderable> renderableComponentMapper;
    private Renderable entityRenderable;

    private float r;
    private float g;
    private float b;

    public ColorManagerSystem() {
        super(Aspect.all(Renderable.class, ManagedColor.class));
        this.setEnabled(false);
    }

    @Override
    protected void process(int entityId) {
        entityRenderable = renderableComponentMapper.get(entityId);
        entityRenderable.setColor(r, g, b);
    }

    protected void setWorldColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.setEnabled(true);
    }
}
