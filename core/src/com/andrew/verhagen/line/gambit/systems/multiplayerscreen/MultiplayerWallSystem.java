package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.components.game.Wall;
import com.andrew.verhagen.line.gambit.screens.MultiplayerScreen;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;

public class MultiplayerWallSystem extends BaseEntitySystem {

    private MultiplayerEntityFactory entityFactory;

    public static final float WALL_THICKNESS = 10f;
    public static final float WALL_START_X = 0;
    public static final float WALL_START_Y = 0;
    public static final float VERTICAL_WALL_HEIGHT = MultiplayerScreen.GAME_WORLD_HEIGHT;
    public static final float HORIZONTAL_WALL_WIDTH = MultiplayerScreen.GAME_WORLD_WIDTH;


    public MultiplayerWallSystem() {
        super(Aspect.all(Wall.class));
        this.setEnabled(false);
    }

    @Override
    protected void initialize() {
        createVerticalWalls();
        createHorizontalWalls();
    }

    private void createVerticalWalls() {
        entityFactory.createWall(0, 0, WALL_THICKNESS, VERTICAL_WALL_HEIGHT);
        entityFactory.createWall(HORIZONTAL_WALL_WIDTH - WALL_THICKNESS, 0, WALL_THICKNESS, VERTICAL_WALL_HEIGHT);
    }

    private void createHorizontalWalls() {
        entityFactory.createWall(0, VERTICAL_WALL_HEIGHT - WALL_THICKNESS, HORIZONTAL_WALL_WIDTH, WALL_THICKNESS);
        entityFactory.createWall(0, 0, HORIZONTAL_WALL_WIDTH, WALL_THICKNESS);
    }


    @Override
    protected void processSystem() {

    }
}
