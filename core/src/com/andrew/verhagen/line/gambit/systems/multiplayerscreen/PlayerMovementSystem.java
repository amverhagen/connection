package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.components.player.Direction;
import com.andrew.verhagen.line.gambit.components.player.MovementDirection;
import com.andrew.verhagen.line.gambit.components.player.Player;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class PlayerMovementSystem extends IteratingSystem {

    private OpponentMovementSystem opponentMovementSystem;
    private ComponentMapper<MovementDirection> movementDirectionComponentMapper;
    private MovementDirection playerDirection;
    private boolean movementSet;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    public PlayerMovementSystem() {
        super(Aspect.all(Player.class, MovementDirection.class));
        movementSet = true;
    }

    @Override
    protected void process(int entityId) {
        if (!movementSet) {
            playerDirection = movementDirectionComponentMapper.get(entityId);
            if (moveLeft) {
                playerDirection.nextDirection = Direction.getLeft(playerDirection.direction);
                //network.setnextinput(left)
                opponentMovementSystem.moveLeft();
                movementSet = true;
            } else if (moveRight) {
                playerDirection.nextDirection = Direction.getRight(playerDirection.direction);
                //network.setnextinput(right)
                opponentMovementSystem.moveRight();
                movementSet = true;
            }
        }
    }

    public void movePlayerLeft() {
        moveLeft = true;
    }

    public void movePlayerRight() {
        moveRight = true;
    }

    public void freeInput() {
        movementSet = false;
        moveLeft = false;
        moveRight = false;
    }
}
