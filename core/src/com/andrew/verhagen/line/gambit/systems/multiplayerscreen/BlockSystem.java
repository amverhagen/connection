package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.components.player.Block;
import com.andrew.verhagen.line.gambit.components.player.Direction;
import com.andrew.verhagen.line.gambit.screens.MultiplayerScreen;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.badlogic.gdx.math.Vector2;

public class BlockSystem extends BaseEntitySystem {

    private MultiplayerEntityFactory entityFactory;
    private MovementPointSystem movementPointSystem;

    private static final float BLOCK_SIDE_LENGTH = MultiplayerScreen.GAME_WORLD_WIDTH * 0.05f;
    private final SpawnLocation playerSpawnLocation;

    public BlockSystem(SpawnLocation playerSpawnLocation) {
        super(Aspect.all(Block.class));
        this.playerSpawnLocation = playerSpawnLocation;
        this.setEnabled(false);
    }

    @Override
    protected void initialize() {
        Vector2 bottomLeftSpawnPoint = movementPointSystem.getBottomLeftSpawnPoint();
        Vector2 topRightSpawnPoint = movementPointSystem.getTopRightSpawnPoint();

        if (this.playerSpawnLocation == SpawnLocation.LEFT) {
            entityFactory.createPlayer((int) bottomLeftSpawnPoint.x, (int) bottomLeftSpawnPoint.y, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH, Direction.RIGHT);
            entityFactory.createOpponent((int) topRightSpawnPoint.x, (int) topRightSpawnPoint.y, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH, Direction.LEFT);

        } else {
            entityFactory.createPlayer((int) topRightSpawnPoint.x, (int) topRightSpawnPoint.y, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH, Direction.LEFT);
            entityFactory.createOpponent((int) bottomLeftSpawnPoint.x, (int) bottomLeftSpawnPoint.y, BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH, Direction.RIGHT);
        }
    }

    @Override
    protected void processSystem() {

    }

    public enum SpawnLocation {
        LEFT,
        RIGHT;
    }
}
