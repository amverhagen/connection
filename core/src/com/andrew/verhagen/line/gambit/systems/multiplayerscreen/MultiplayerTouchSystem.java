package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.input.TouchEvent;
import com.andrew.verhagen.line.gambit.systems.input.TouchListenerSystem;
import com.artemis.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MultiplayerTouchSystem extends TouchListenerSystem {

    public static final String LEFT_INPUT = "leftInput";
    public static final String RIGHT_INPUT = "rightInput";
    private GambitGame gameInstance;


    public MultiplayerTouchSystem(Viewport viewport, GambitGame gameInstance) {
        super(viewport);
        this.gameInstance = gameInstance;
    }

    @Override
    public void setCustomEvents() {
        TouchEvent leftInput = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                return false;
            }
        };
        this.touchEventHashMap.put(LEFT_INPUT, leftInput);

        TouchEvent rightInput = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                return false;
            }
        };
        this.touchEventHashMap.put(RIGHT_INPUT, rightInput);
    }
}
