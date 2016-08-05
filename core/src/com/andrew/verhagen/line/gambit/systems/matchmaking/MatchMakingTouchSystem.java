package com.andrew.verhagen.line.gambit.systems.matchmaking;

import com.andrew.verhagen.line.gambit.components.input.TouchEvent;
import com.andrew.verhagen.line.gambit.systems.input.TouchListenerSystem;
import com.artemis.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MatchMakingTouchSystem extends TouchListenerSystem {

    private ConnectionSystem connectionSystem;
    public static final String retryConnection = "retryConnection";
    public static final String cancelConnection = "cancelConnection";

    public MatchMakingTouchSystem(Viewport viewport) {
        super(viewport);
    }

    @Override
    public void setCustomEvents() {

        TouchEvent retryConnectionEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                connectionSystem.retryConnection();
                System.out.println("hello");
                return true;
            }
        };
        this.touchEventHashMap.put(retryConnection, retryConnectionEvent);

        TouchEvent cancelConnectionEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                connectionSystem.endConnection();
                return true;
            }
        };
        this.touchEventHashMap.put(cancelConnection, cancelConnectionEvent);
    }
}
