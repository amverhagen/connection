package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.screens.MultiplayerScreen;
import com.artemis.BaseSystem;
import com.badlogic.gdx.math.Vector2;

public class MovementPointSystem extends BaseSystem {

    private MultiplayerEntityFactory entityFactory;

    public static final int VERTICAL_POINTS = 18;
    public static final int HORIZONTAL_POINTS = 32;
    private final float verticalGap = MultiplayerScreen.GAME_WORLD_HEIGHT / (float) (VERTICAL_POINTS - 1);
    private final float horizontalGap = MultiplayerScreen.GAME_WORLD_WIDTH / (float) (HORIZONTAL_POINTS - 1);
    private Vector2[] points = new Vector2[VERTICAL_POINTS * HORIZONTAL_POINTS];

    @Override
    protected void initialize() {
        float currentXPosition;
        float currentYPosition;
        for (int i = 0; i < VERTICAL_POINTS; i++) {
            currentYPosition = i * verticalGap;
            for (int j = 0; j < HORIZONTAL_POINTS; j++) {
                currentXPosition = j * horizontalGap;
                points[(i * HORIZONTAL_POINTS) + j] = new Vector2(currentXPosition, currentYPosition);
                entityFactory.createMovementPoint(currentXPosition, currentYPosition);
            }
        }
    }

    public Vector2 getBottomLeftSpawnPoint() {
        return new Vector2(2, 2);
    }

    public Vector2 getTopRightSpawnPoint() {
        return new Vector2(HORIZONTAL_POINTS - 3, VERTICAL_POINTS - 3);
    }

    public Vector2 getPointAt(int x, int y) {
        return points[(y * HORIZONTAL_POINTS) + x];
    }

    @Override
    protected void processSystem() {

    }
}
