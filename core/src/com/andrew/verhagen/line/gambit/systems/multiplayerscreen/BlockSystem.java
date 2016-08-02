package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.components.player.Block;
import com.andrew.verhagen.line.gambit.screens.MultiplayerScreen;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;

public class BlockSystem extends BaseEntitySystem {

    private MultiplayerEntityFactory entityFactory;

    private static final float BLOCK_SIDE_LENGTH = MultiplayerScreen.GAME_WORLD_WIDTH * 0.05f;
    private final SpawnLocation playerSpawnLocation;

    public BlockSystem(SpawnLocation playerSpawnLocation) {
        super(Aspect.all(Block.class));
        this.playerSpawnLocation = playerSpawnLocation;
        this.setEnabled(false);
    }

    @Override
    protected void initialize() {
        if (this.playerSpawnLocation == SpawnLocation.LEFT) {
            entityFactory.createPlayer(SpawnLocation.LEFT.xSpawnLocation, SpawnLocation.LEFT.ySpawnLocation, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH);
            entityFactory.createOpponent(SpawnLocation.RIGHT.xSpawnLocation, SpawnLocation.RIGHT.ySpawnLocation, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH);

        } else {
            entityFactory.createPlayer(SpawnLocation.RIGHT.xSpawnLocation, SpawnLocation.RIGHT.ySpawnLocation, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH);
            entityFactory.createOpponent(SpawnLocation.LEFT.xSpawnLocation, SpawnLocation.LEFT.ySpawnLocation, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH);

        }
    }

    @Override
    protected void processSystem() {

    }

    public enum SpawnLocation {
        LEFT(
                MultiplayerWallSystem.WALL_START_X + MultiplayerWallSystem.WALL_THICKNESS + BLOCK_SIDE_LENGTH,
                MultiplayerWallSystem.WALL_START_Y + MultiplayerWallSystem.WALL_THICKNESS + BLOCK_SIDE_LENGTH),
        RIGHT(
                MultiplayerWallSystem.WALL_START_X + MultiplayerWallSystem.HORIZONTAL_WALL_WIDTH - (MultiplayerWallSystem.WALL_THICKNESS + BLOCK_SIDE_LENGTH * 2),
                MultiplayerWallSystem.WALL_START_Y + MultiplayerWallSystem.VERTICAL_WALL_HEIGHT - (MultiplayerWallSystem.WALL_THICKNESS + BLOCK_SIDE_LENGTH * 2));

        final float xSpawnLocation;
        final float ySpawnLocation;

        SpawnLocation(float xSpawnLocation, float ySpawnLocation) {
            this.xSpawnLocation = xSpawnLocation;
            this.ySpawnLocation = ySpawnLocation;
        }
    }
}
