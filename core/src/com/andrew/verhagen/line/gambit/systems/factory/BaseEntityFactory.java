package com.andrew.verhagen.line.gambit.systems.factory;

import com.andrew.verhagen.line.gambit.components.graphics.Renderable;
import com.andrew.verhagen.line.gambit.components.input.Touchable;
import com.andrew.verhagen.line.gambit.components.positional.Bounds;
import com.andrew.verhagen.line.gambit.components.positional.Position;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;

public abstract class BaseEntityFactory extends BaseSystem {

    protected ArchetypeHolder archetypeHolder;
    public static final String boundedBox = "bounded_box";
    public static final String renderable = "renderable";
    public static final String button = "button";
    private ComponentMapper<Position> positionComponentMapper;
    private ComponentMapper<Bounds> boundsComponentMapper;
    private ComponentMapper<Renderable> renderableComponentMapper;
    private ComponentMapper<Touchable> touchableComponentMapper;

    public BaseEntityFactory() {
        this.setEnabled(false);
    }

    @Override
    protected void initialize() {
        archetypeHolder = new ArchetypeHolder(world);
        archetypeHolder.createArchetype(boundedBox, Position.class, Bounds.class);
        archetypeHolder.createArchetype(renderable, boundedBox, Renderable.class);
        archetypeHolder.createArchetype(button, renderable, Touchable.class);
        createCustomArchetypes();
    }

    protected abstract void createCustomArchetypes();

    public int createRenderable(float x, float y, float width, float height, String texture) {
        int entityId = world.create(archetypeHolder.getArchetype(renderable));
        return editRenderable(entityId, x, y, width, height, texture);
    }

    public int editRenderable(int entityId, float x, float y, float width, float height, String texture) {
        Position entityPosition = positionComponentMapper.get(entityId);
        entityPosition.x = x;
        entityPosition.y = y;

        Bounds entityBounds = boundsComponentMapper.get(entityId);
        entityBounds.width = width;
        entityBounds.height = height;

        Renderable entityRenderable = renderableComponentMapper.get(entityId);
        entityRenderable.textureName = texture;

        return entityId;
    }

    public int createButton(float x, float y, float width, float height, String texture, String eventName) {
        int entityId = world.create(archetypeHolder.getArchetype(button));
        return editButton(entityId, x, y, width, height, texture, eventName);
    }

    public int editButton(int entityId, float x, float y, float width, float height, String texture, String eventName) {
        Touchable entityTouch = touchableComponentMapper.get(entityId);
        entityTouch.touchEventName = eventName;
        return editRenderable(entityId, x, y, width, height, texture);
    }

    @Override
    protected void processSystem() {

    }
}
