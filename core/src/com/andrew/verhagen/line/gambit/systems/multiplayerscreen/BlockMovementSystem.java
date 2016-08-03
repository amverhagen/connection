package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.components.player.Block;
import com.andrew.verhagen.line.gambit.components.player.Direction;
import com.andrew.verhagen.line.gambit.components.player.MovementDirection;
import com.andrew.verhagen.line.gambit.components.player.Player;
import com.andrew.verhagen.line.gambit.components.positional.Bounds;
import com.andrew.verhagen.line.gambit.components.positional.Position;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

public class BlockMovementSystem extends IteratingSystem {
    private PlayerMovementSystem playerMovementSystem;
    private MovementPointSystem movementPointSystem;
    private float blockUnitsPerTick = 5f;

    private ComponentMapper<Player> playerComponentMapper;
    private ComponentMapper<Bounds> boundsComponentMapper;
    private ComponentMapper<Block> blockComponentMapper;
    private ComponentMapper<Position> positionComponentMapper;
    private ComponentMapper<MovementDirection> movementDirectionComponentMapper;
    private MovementDirection blockDirection;
    private Position blockPosition;
    private Bounds blockBounds;
    private float distance;
    private Vector2 blockCenter;

    public BlockMovementSystem() {
        super(Aspect.all(Block.class, MovementDirection.class));
        blockCenter = new Vector2();
    }

    @Override
    protected void process(int entityId) {
        blockDirection = movementDirectionComponentMapper.get(entityId);
        blockPosition = positionComponentMapper.get(entityId);
        blockBounds = boundsComponentMapper.get(entityId);
        blockCenter.set(blockPosition.x + blockBounds.width / 2f, blockPosition.y + blockBounds.height / 2f);
        distance = Vector2.dst(blockCenter.x, blockCenter.y, blockDirection.xDestination, blockDirection.yDestination);

        if (distance <= blockUnitsPerTick) {
            if (playerComponentMapper.has(entityId)) {
                playerMovementSystem.freeInput();
            }
            blockDirection.direction = blockDirection.nextDirection;
            blockDirection.nextDirection = blockDirection.direction;
            if (blockDirection.direction == Direction.UP) {
                blockDirection.yCord++;
            } else if (blockDirection.direction == Direction.RIGHT) {
                blockDirection.xCord++;
            } else if (blockDirection.direction == Direction.DOWN) {
                blockDirection.yCord--;
            } else if (blockDirection.direction == Direction.LEFT) {
                blockDirection.xCord--;
            }
            blockPosition.x = blockDirection.xDestination - blockBounds.width / 2f;
            blockPosition.y = blockDirection.yDestination - blockBounds.height / 2f;

            try {
                Vector2 destination = movementPointSystem.getPointAt(blockDirection.xCord, blockDirection.yCord);
                blockDirection.xDestination = destination.x;
                blockDirection.yDestination = destination.y;
            } catch (IndexOutOfBoundsException e) {
                movementDirectionComponentMapper.remove(entityId);
            }
        }

        if (blockDirection.direction == Direction.UP) {
            blockPosition.y += blockUnitsPerTick;
        } else if (blockDirection.direction == Direction.RIGHT) {
            blockPosition.x += blockUnitsPerTick;
        } else if (blockDirection.direction == Direction.DOWN) {
            blockPosition.y -= blockUnitsPerTick;
        } else if (blockDirection.direction == Direction.LEFT) {
            blockPosition.x -= blockUnitsPerTick;
        }
    }
}
