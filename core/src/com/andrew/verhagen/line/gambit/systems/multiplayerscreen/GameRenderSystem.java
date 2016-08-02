package com.andrew.verhagen.line.gambit.systems.multiplayerscreen;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.game.InGame;
import com.andrew.verhagen.line.gambit.systems.graphics.RenderSystem;
import com.artemis.Aspect;
import com.badlogic.gdx.graphics.Camera;

public class GameRenderSystem extends RenderSystem {
    public GameRenderSystem(GambitGame gameInstance, Camera gameCamera) {
        super(gameInstance, gameCamera, Aspect.all(InGame.class));
    }
}
