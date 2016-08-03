package com.andrew.verhagen.line.gambit.systems.input;

import com.andrew.verhagen.line.gambit.components.input.TouchEvent;
import com.andrew.verhagen.line.gambit.components.input.Touchable;
import com.andrew.verhagen.line.gambit.components.positional.Bounds;
import com.andrew.verhagen.line.gambit.components.positional.Position;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TouchListenerSystem extends IteratingSystem implements InputProcessor {

    private ComponentMapper<Position> positionComponentMapper;
    private Position entityPosition;
    private ComponentMapper<Bounds> boundsComponentMapper;
    private Bounds entityBounds;
    private ComponentMapper<Touchable> touchableComponentMapper;
    private Touchable entityTouch;

    private Viewport viewport;
    private Vector2 touchPoint;
    private TouchEvent currentTouchEvent;

    protected HashMap<String, TouchEvent> touchEventHashMap;
    private ArrayList<Integer> touchedEntityIds;

    public TouchListenerSystem(Viewport viewport) {
        super(Aspect.all(Touchable.class, Position.class, Bounds.class));
        this.viewport = viewport;
        this.setEnabled(false);
        this.touchPoint = new Vector2();
        this.touchEventHashMap = new HashMap<String, TouchEvent>();
        this.touchEventHashMap.put(null, null);
        this.touchedEntityIds = new ArrayList<Integer>();
        this.setCustomEvents();
    }

    @Override
    protected void begin() {
        this.touchedEntityIds.clear();
    }

    @Override
    protected void process(int entityId) {
        entityTouch = touchableComponentMapper.get(entityId);
        entityBounds = boundsComponentMapper.get(entityId);
        entityPosition = positionComponentMapper.get(entityId);

        if (containsPoint(entityBounds, entityPosition, touchPoint))
            this.touchedEntityIds.add(entityId);
    }

    @Override
    protected void end() {
        for (int i = touchedEntityIds.size() - 1; i >= 0; i--) {
            int entityId = touchedEntityIds.get(i);
            entityTouch = touchableComponentMapper.get(entityId);
            currentTouchEvent = touchEventHashMap.get(entityTouch.touchEventName);
            if (currentTouchEvent != null) {
                if (currentTouchEvent.touched(world, entityId, touchPoint.x, touchPoint.y))
                    break;
            }
        }
        this.setEnabled(false);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPoint.set(screenX, screenY);
        viewport.unproject(touchPoint);
        this.setEnabled(true);
        return true;
    }

    public boolean containsPoint(Bounds entityBounds, Position entityPosition, Vector2 point) {
        return entityPosition.x <= point.x && entityPosition.x + entityBounds.width >= point.x
                && entityPosition.y <= point.y && entityPosition.y + entityBounds.height >= point.y;
    }

    public abstract void setCustomEvents();

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
