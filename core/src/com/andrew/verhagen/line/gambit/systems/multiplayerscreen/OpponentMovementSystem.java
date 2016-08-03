package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.components.player.Direction;
import com.andrew.verhagen.line.gambit.components.player.MovementDirection;
import com.andrew.verhagen.line.gambit.components.player.Opponent;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class OpponentMovementSystem extends IteratingSystem {

    private ComponentMapper<MovementDirection> movementDirectionComponentMapper;
    private MovementDirection opponentDirection;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    public OpponentMovementSystem() {
        super(Aspect.all(Opponent.class, MovementDirection.class));
    }

    @Override
    protected void process(int entityId) {
        if (moveLeft || moveRight) {
            opponentDirection = movementDirectionComponentMapper.get(entityId);
            if (moveLeft) {
                opponentDirection.nextDirection = Direction.getLeft(opponentDirection.direction);
            } else {
                opponentDirection.nextDirection = Direction.getRight(opponentDirection.direction);
            }
            moveLeft = false;
            moveRight = false;
        }
    }

    public void moveLeft() {
        moveLeft = true;
    }

    public void moveRight() {
        moveRight = true;
    }
}
