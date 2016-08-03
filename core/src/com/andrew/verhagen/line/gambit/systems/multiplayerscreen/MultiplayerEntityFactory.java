package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.components.game.InGame;
import com.andrew.verhagen.line.gambit.components.game.MovementPoint;
import com.andrew.verhagen.line.gambit.components.game.Wall;
import com.andrew.verhagen.line.gambit.components.home.ManagedColor;
import com.andrew.verhagen.line.gambit.components.player.Block;
import com.andrew.verhagen.line.gambit.components.player.Direction;
import com.andrew.verhagen.line.gambit.components.player.Opponent;
import com.andrew.verhagen.line.gambit.components.player.Player;
import com.andrew.verhagen.line.gambit.components.player.MovementDirection;
import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.andrew.verhagen.line.gambit.systems.factory.BaseEntityFactory;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;

public class MultiplayerEntityFactory extends BaseEntityFactory {

    private MovementPointSystem movementPointSystem;
    private ComponentMapper<MovementDirection> movementDirectionComponentMapper;
    public static final String PLAYER = "player";
    public static final String WALL = "wall";
    public static final String OPPONENT = "opponent";
    public static final String MOVEMENT_POINT = "movementPoint";

    @Override
    protected void createCustomArchetypes() {
        archetypeHolder.createArchetype(PLAYER, BaseEntityFactory.COLLIDABLE, Block.class, MovementDirection.class, ManagedColor.class, InGame.class, Player.class);
        archetypeHolder.createArchetype(OPPONENT, BaseEntityFactory.COLLIDABLE, Block.class, MovementDirection.class, InGame.class, Opponent.class);
        archetypeHolder.createArchetype(WALL, BaseEntityFactory.COLLIDABLE, Wall.class, ManagedColor.class, InGame.class);
        archetypeHolder.createArchetype(MOVEMENT_POINT, BaseEntityFactory.renderable, InGame.class, MovementPoint.class);
    }

    public int createWall(float x, float y, float width, float height) {
        int wallId = world.create(archetypeHolder.getArchetype(WALL));
        return super.editRenderable(wallId, x, y, width, height, Assets.white);
    }

    public int createPlayer(int xCord, int yCord, float width, float height, Direction movementDirection) {
        int playerId = world.create(archetypeHolder.getArchetype(PLAYER));
        MovementDirection playerDirection = movementDirectionComponentMapper.get(playerId);
        playerDirection.nextDirection = movementDirection;
        playerDirection.xCord = xCord;
        playerDirection.yCord = yCord;
        Vector2 destination = movementPointSystem.getPointAt(xCord, yCord);
        System.out.println(destination);
        System.out.println("Cords " + xCord + " " + yCord);
        playerDirection.xDestination = destination.x;
        playerDirection.yDestination = destination.y;

        return super.editRenderable(playerId, destination.x - width / 2, destination.y - height / 2, width, height, Assets.white);
    }

    public int createOpponent(int xCord, int yCord, float width, float height, Direction movementDirection) {
        int opponentId = world.create(archetypeHolder.getArchetype(OPPONENT));
        MovementDirection opponentDirection = movementDirectionComponentMapper.get(opponentId);
        opponentDirection.nextDirection = movementDirection;
        opponentDirection.xCord = xCord;
        opponentDirection.yCord = yCord;
        Vector2 destination = movementPointSystem.getPointAt(xCord, yCord);
        opponentDirection.xDestination = destination.x;
        opponentDirection.yDestination = destination.y;
        movementDirectionComponentMapper.get(opponentId).direction = movementDirection;
        return super.editRenderable(opponentId, destination.x - width / 2, destination.y - height / 2, width, height, Assets.white);
    }

    public int createMovementPoint(float x, float y) {
        int pointId = world.create(archetypeHolder.getArchetype(MOVEMENT_POINT));
        return super.editRenderable(pointId, x, y, 10f, 10f, Assets.clear);
    }
}
