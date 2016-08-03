package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.input.TouchEvent;
import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.andrew.verhagen.line.gambit.screens.MultiplayerScreen;
import com.andrew.verhagen.line.gambit.systems.input.TouchListenerSystem;
import com.artemis.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MultiplayerTouchSystem extends TouchListenerSystem {

    private MultiplayerEntityFactory entityFactory;
    private PlayerMovementSystem playerMovementSystem;

    public static final String LEFT_INPUT = "leftInput";
    public static final String RIGHT_INPUT = "rightInput";


    public MultiplayerTouchSystem(Viewport viewport, GambitGame gameInstance) {
        super(viewport);
    }

    @Override
    protected void initialize() {
        float halfWidth = MultiplayerScreen.GAME_WORLD_WIDTH / 2f;
        entityFactory.createButton(
                0, 0,
                halfWidth, MultiplayerScreen.GAME_WORLD_HEIGHT,
                Assets.clear, LEFT_INPUT);
        entityFactory.createButton(
                halfWidth, 0,
                halfWidth, MultiplayerScreen.GAME_WORLD_HEIGHT,
                Assets.clear, RIGHT_INPUT);
    }

    @Override
    public void setCustomEvents() {
        TouchEvent leftInput = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                playerMovementSystem.movePlayerLeft();
                return true;
            }
        };
        this.touchEventHashMap.put(LEFT_INPUT, leftInput);

        TouchEvent rightInput = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                playerMovementSystem.movePlayerRight();
                return true;
            }
        };
        this.touchEventHashMap.put(RIGHT_INPUT, rightInput);
    }
}
