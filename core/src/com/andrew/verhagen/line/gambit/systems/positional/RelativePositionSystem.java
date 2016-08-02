package com.andrew.verhagen.line.gambit.systems.positional;

import com.andrew.verhagen.line.gambit.components.positional.Position;
import com.andrew.verhagen.line.gambit.components.positional.RelativePosition;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

public class RelativePositionSystem extends IteratingSystem {
    private ComponentMapper<Position> positionComponentMapper;
    private Position parentPosition;
    private Position entityPosition;
    private ComponentMapper<RelativePosition> relativePositionComponentMapper;
    private RelativePosition relativePosition;

    public RelativePositionSystem() {
        super(Aspect.all(Position.class, RelativePosition.class));
    }

    @Override
    protected void process(int entityId) {
        entityPosition = positionComponentMapper.get(entityId);
        relativePosition = relativePositionComponentMapper.get(entityId);
        parentPosition = positionComponentMapper.get(relativePosition.parentId);

        entityPosition.x = parentPosition.x + relativePosition.relativeX;
        entityPosition.y = parentPosition.y + relativePosition.relativeY;
    }
}
