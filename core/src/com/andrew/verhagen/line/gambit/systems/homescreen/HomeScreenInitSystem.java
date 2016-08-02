package com.andrew.verhagen.line.gambit.systems.homescreen;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.graphics.Renderable;
import com.andrew.verhagen.line.gambit.components.home.ManagedColor;
import com.andrew.verhagen.line.gambit.components.movement.MoveToPoint;
import com.andrew.verhagen.line.gambit.components.movement.MovementSpeed;
import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.andrew.verhagen.line.gambit.gameutils.GameColors;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;

public class HomeScreenInitSystem extends BaseSystem {
    private HomeEntityFactory entityFactory;
    private ComponentMapper<Renderable> renderableComponentMapper;
    private ComponentMapper<MovementSpeed> movementSpeedComponentMapper;
    private ComponentMapper<MoveToPoint> moveToPointComponentMapper;
    private ComponentMapper<ManagedColor> managedColorComponentMapper;

    private float iconWidth = 100f;
    private float buttonWidth = 320f;
    private float buttonHeight = 160f;

    @Override
    protected void initialize() {
        createBackgroundButton();
        createTitle();
        createOptionsIcon();
        createPlayButtons();
        createMusicIcon();
    }

    private void createBackgroundButton() {
        entityFactory.createButton(
                0, 0,
                GambitGame.UI_WIDTH, GambitGame.UI_HEIGHT,
                Assets.black, HomeTouchSystem.hidePanel);
    }

    private void createTitle() {
        entityFactory.createManagedColorRenderable(
                0, GambitGame.UI_HEIGHT * 0.4f,
                GambitGame.UI_WIDTH, GambitGame.UI_HEIGHT * 0.6f,
                Assets.gambitTittle);
    }


    private void createPlayButtons() {
        int onePlayer = entityFactory.createButton(
                (GambitGame.UI_WIDTH * 0.45f) - buttonWidth, GambitGame.UI_HEIGHT * 0.2f,
                buttonWidth, buttonHeight,
                Assets.onePlayerButton, HomeTouchSystem.changeToSinglePlayer);
        managedColorComponentMapper.create(onePlayer);

        int multiPlayer = entityFactory.createButton(
                (GambitGame.UI_WIDTH * 0.55f), GambitGame.UI_HEIGHT * 0.2f,
                buttonWidth, buttonHeight,
                Assets.twoPlayerButton, HomeTouchSystem.changeToMultiplayerScreen);
        managedColorComponentMapper.create(multiPlayer);
    }

    private void createOptionsIcon() {
        int options = entityFactory.createButton(
                GambitGame.UI_WIDTH * 0.065f - iconWidth, GambitGame.UI_HEIGHT * 0.05f,
                iconWidth, iconWidth,
                Assets.options, HomeTouchSystem.openPanel);
        managedColorComponentMapper.create(options);
    }

    private void createMusicIcon() {
        int music = entityFactory.createRenderable(
                GambitGame.UI_WIDTH * 0.935f, GambitGame.UI_HEIGHT * 0.05f,
                iconWidth, iconWidth,
                Assets.musicIcon
        );
        managedColorComponentMapper.create(music);
    }

    public void createColorButtons(int panelId, float panelWidth, float panelHeight) {
        float colorRelativeX = panelWidth * 0.1f;
        float colorRelativeYGap = panelHeight * 0.06f;
        float buttonWidth = panelWidth * 0.8f;

        int redButton = entityFactory.createRelativeButton(
                colorRelativeX, colorRelativeYGap,
                buttonWidth, buttonWidth,
                Assets.white, HomeTouchSystem.changeWorldColor, panelId);
        Renderable buttonRenderable = renderableComponentMapper.get(redButton);
        buttonRenderable.setColor(GameColors.GAMBIT_RED);

        int blueButton = entityFactory.createRelativeButton(
                colorRelativeX, colorRelativeYGap * 2 + buttonWidth,
                buttonWidth, buttonWidth,
                Assets.white, HomeTouchSystem.changeWorldColor, panelId);
        buttonRenderable = renderableComponentMapper.get(blueButton);
        buttonRenderable.setColor(GameColors.GAMBIT_BLUE);

        int greenButton = entityFactory.createRelativeButton(
                colorRelativeX, colorRelativeYGap * 3 + buttonWidth * 2,
                buttonWidth, buttonWidth,
                Assets.white, HomeTouchSystem.changeWorldColor, panelId);
        buttonRenderable = renderableComponentMapper.get(greenButton);
        buttonRenderable.setColor(GameColors.GAMBIT_GREEN);

        int whiteButton = entityFactory.createRelativeButton(
                colorRelativeX, colorRelativeYGap * 4 + buttonWidth * 3,
                buttonWidth, buttonWidth,
                Assets.white, HomeTouchSystem.changeWorldColor, panelId);
        buttonRenderable = renderableComponentMapper.get(whiteButton);
        buttonRenderable.setColor(GameColors.WHITE);
    }

    @Override
    protected void processSystem() {

    }
}
