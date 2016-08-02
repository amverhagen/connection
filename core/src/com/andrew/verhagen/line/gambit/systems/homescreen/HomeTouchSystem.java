package com.andrew.verhagen.line.gambit.systems.homescreen;

import com.andrew.verhagen.line.gambit.components.graphics.Renderable;
import com.andrew.verhagen.line.gambit.components.input.TouchEvent;
import com.andrew.verhagen.line.gambit.systems.input.TouchListenerSystem;
import com.artemis.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HomeTouchSystem extends TouchListenerSystem {
    private ColorPanelSystem colorPanelSystem;
    private ColorManagerSystem colorManagerSystem;
    public static final String changeWorldColor = "changeWorldColor";
    public static final String openPanel = "openPanel";
    public static final String hidePanel = "hidePanel";

    public HomeTouchSystem(Viewport viewport) {
        super(viewport);
    }

    @Override
    public void setCustomEvents() {
        TouchEvent openPanelEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                colorPanelSystem.showPanel();
                return true;
            }
        };
        this.touchEventHashMap.put(openPanel, openPanelEvent);

        TouchEvent hidePanelEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                colorPanelSystem.hidePanel();
                return true;
            }
        };

        this.touchEventHashMap.put(hidePanel, hidePanelEvent);

        TouchEvent changeWorldColorEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                Renderable touchedRenderable = world.getMapper(Renderable.class).get(id);
                colorManagerSystem.setWorldColor(touchedRenderable.r, touchedRenderable.g, touchedRenderable.b);
                return true;
            }
        };

        this.touchEventHashMap.put(changeWorldColor, changeWorldColorEvent);
    }
}
