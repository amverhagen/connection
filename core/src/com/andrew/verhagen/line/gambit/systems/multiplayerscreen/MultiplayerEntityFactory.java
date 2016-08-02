package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.components.game.InGame;
import com.andrew.verhagen.line.gambit.components.game.Wall;
import com.andrew.verhagen.line.gambit.components.home.ManagedColor;
import com.andrew.verhagen.line.gambit.components.player.Opponent;
import com.andrew.verhagen.line.gambit.components.player.Player;
import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.andrew.verhagen.line.gambit.systems.factory.BaseEntityFactory;

public class MultiplayerEntityFactory extends BaseEntityFactory {

    public static final String PLAYER = "player";
    public static final String WALL = "wall";
    public static final String OPPONENT = "opponent";

    @Override
    protected void createCustomArchetypes() {
        archetypeHolder.createArchetype(PLAYER, BaseEntityFactory.COLLIDABLE, ManagedColor.class, InGame.class, Player.class);
        archetypeHolder.createArchetype(OPPONENT, BaseEntityFactory.COLLIDABLE, InGame.class, Opponent.class);
        archetypeHolder.createArchetype(WALL, BaseEntityFactory.COLLIDABLE, Wall.class, ManagedColor.class, InGame.class);
    }

    public int createWall(float x, float y, float width, float height) {
        int wallId = world.create(archetypeHolder.getArchetype(WALL));
        return super.editRenderable(wallId, x, y, width, height, Assets.white);
    }

    public int createPlayer(float x, float y, float width, float height) {
        int playerId = world.create(archetypeHolder.getArchetype(PLAYER));
        return super.editRenderable(playerId, x, y, width, height, Assets.white);
    }

    public int createOpponent(float x, float y, float width, float height) {
        int opponentId = world.create(archetypeHolder.getArchetype(OPPONENT));
        return super.editRenderable(opponentId, x, y, width, height, Assets.white);
    }
}
