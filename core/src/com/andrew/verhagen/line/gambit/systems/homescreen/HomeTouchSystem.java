package com.andrew.verhagen.line.gambit.systems.homescreen;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.graphics.Renderable;
import com.andrew.verhagen.line.gambit.components.input.TouchEvent;
import com.andrew.verhagen.line.gambit.screens.MatchMakingScreen;
import com.andrew.verhagen.line.gambit.screens.MultiplayerScreen;
import com.andrew.verhagen.line.gambit.systems.input.TouchListenerSystem;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HomeTouchSystem extends TouchListenerSystem {
    private ColorPanelSystem colorPanelSystem;
    private com.andrew.verhagen.line.gambit.systems.graphics.ColorManagerSystem colorManagerSystem;
    private GambitGame gameInstance;
    public static final String changeWorldColor = "changeWorldColor";
    public static final String openPanel = "openPanel";
    public static final String hidePanel = "hidePanel";
    public static final String changeToMultiplayerScreen = "multiplayerScreen";
    public static final String changeToSinglePlayer = "singlePlayer";

    public HomeTouchSystem(Viewport viewport, GambitGame gambitGame) {
        super(viewport);
        this.gameInstance = gambitGame;
    }

    @Override
    public void setCustomEvents() {
        TouchEvent openPanelEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                colorPanelSystem.showPanel();
                return true;
            }
        };
        this.touchEventHashMap.put(openPanel, openPanelEvent);

        TouchEvent hidePanelEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                colorPanelSystem.hidePanel();
                return true;
            }
        };

        this.touchEventHashMap.put(hidePanel, hidePanelEvent);

        TouchEvent changeWorldColorEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                Renderable touchedRenderable = world.getMapper(Renderable.class).get(id);
                colorManagerSystem.setWorldColor(touchedRenderable.r, touchedRenderable.g, touchedRenderable.b);
                return true;
            }
        };

        this.touchEventHashMap.put(changeWorldColor, changeWorldColorEvent);

        TouchEvent changeToSinglePlayerScreenEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                Gdx.app.log("Touched", "single player");
                return true;
            }
        };
        this.touchEventHashMap.put(changeToSinglePlayer, changeToSinglePlayerScreenEvent);

        TouchEvent changeToMultiplayerScreenEvent = new TouchEvent() {
            @Override
            public boolean touched(World world, int id, float touchX, float touchY) {
                gameInstance.screenManager.setScreen(MatchMakingScreen.class);
                return true;
            }
        };
        this.touchEventHashMap.put(changeToMultiplayerScreen, changeToMultiplayerScreenEvent);
    }
}
