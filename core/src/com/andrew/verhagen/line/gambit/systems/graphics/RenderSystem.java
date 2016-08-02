package com.andrew.verhagen.line.gambit.systems.graphics;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.graphics.Renderable;
import com.andrew.verhagen.line.gambit.components.positional.Bounds;
import com.andrew.verhagen.line.gambit.components.positional.Position;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderSystem extends IteratingSystem {

    private ComponentMapper<Renderable> renderableComponentMapper;
    private Renderable entityRenderable;
    private ComponentMapper<Position> positionComponentMapper;
    private Position entityPosition;
    private ComponentMapper<Bounds> boundsComponentMapper;
    private Bounds entityBounds;

    private GambitGame gameInstance;
    private SpriteBatch batch;

    public RenderSystem(GambitGame gameInstance) {
        super(Aspect.all(Position.class, Bounds.class, Renderable.class));
        this.gameInstance = gameInstance;
        this.batch = new SpriteBatch();
    }

    @Override
    protected void inserted(int entityId) {
        entityRenderable = renderableComponentMapper.get(entityId);
        if (entityRenderable.texture == null)
            entityRenderable.texture = gameInstance.assets.manager.get(entityRenderable.textureName, Texture.class);
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(gameInstance.uiCamera.combined);
        batch.begin();
    }

    @Override
    protected void process(int entityId) {
        entityRenderable = renderableComponentMapper.get(entityId);
        entityPosition = positionComponentMapper.get(entityId);
        entityBounds = boundsComponentMapper.get(entityId);
        drawCurrentEntity();
    }

    private void drawCurrentEntity() {

        batch.setColor(entityRenderable.r, entityRenderable.g, entityRenderable.b, entityRenderable.a);
        batch.draw(
                entityRenderable.texture,
                entityPosition.x,
                entityPosition.y,
                0,
                0,
                entityBounds.width,
                entityBounds.height,
                1,
                1,
                0,
                0, 0,
                entityRenderable.texture.getWidth(),
                entityRenderable.texture.getHeight(),
                false, false);
    }

    @Override
    protected void end() {
        batch.end();
    }
}
