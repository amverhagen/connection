package com.andrew.verhagen.line.gambit.systems.graphics;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.graphics.Renderable;
import com.andrew.verhagen.line.gambit.components.home.ManagedColor;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class ColorManagerSystem extends IteratingSystem {
    private ComponentMapper<Renderable> renderableComponentMapper;
    private Renderable entityRenderable;

    private GambitGame gameInstance;

    public ColorManagerSystem(GambitGame gambitGame) {
        super(Aspect.all(Renderable.class, ManagedColor.class));
        this.gameInstance = gambitGame;
    }

    @Override
    protected void process(int entityId) {
        entityRenderable = renderableComponentMapper.get(entityId);
        entityRenderable.setColor(gameInstance.gameColor);
    }

    @Override
    protected void end() {
        this.setEnabled(false);
    }

    public void setWorldColor(float r, float g, float b) {
        gameInstance.setGameColor(r, g, b);
        this.setEnabled(true);
    }
}
