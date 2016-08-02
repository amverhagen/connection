package com.andrew.verhagen.line.gambit.systems.movement;

import com.andrew.verhagen.line.gambit.components.movement.MoveToPoint;
import com.andrew.verhagen.line.gambit.components.movement.MovementSpeed;
import com.andrew.verhagen.line.gambit.components.positional.Position;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class MoveToPointSystem extends IteratingSystem {
    private ComponentMapper<MoveToPoint> moveToPointComponentMapper;
    private MoveToPoint entityDestination;
    private ComponentMapper<MovementSpeed> movementSpeedComponentMapper;
    private MovementSpeed entitySpeed;
    private ComponentMapper<Position> positionComponentMapper;
    private Position entityPosition;

    private float unitsToMove;
    private float xDistance;
    private float yDistance;
    private float distance;
    private float progress;

    public MoveToPointSystem() {
        super(Aspect.all(Position.class, MoveToPoint.class, MovementSpeed.class));
    }

    @Override
    protected void process(int entityId) {
        entityPosition = positionComponentMapper.get(entityId);
        entityDestination = moveToPointComponentMapper.get(entityId);
        entitySpeed = movementSpeedComponentMapper.get(entityId);

        unitsToMove = entitySpeed.unitsPerSecond * world.getDelta();
        xDistance = entityDestination.destinationX - entityPosition.x;
        yDistance = entityDestination.destinationY - entityPosition.y;
        distance = (float) Math.sqrt(xDistance * xDistance + yDistance * yDistance);

        if (unitsToMove >= distance) {
            entityPosition.x = entityDestination.destinationX;
            entityPosition.y = entityDestination.destinationY;
            moveToPointComponentMapper.remove(entityId);
        } else {
            progress = unitsToMove / distance;
            entityPosition.x += xDistance * progress;
            entityPosition.y += yDistance * progress;
        }
    }
}
