package com.andrew.verhagen.line.gambit.systems.homescreen;

import com.andrew.verhagen.line.gambit.GambitGame;
import com.andrew.verhagen.line.gambit.components.graphics.Renderable;
import com.andrew.verhagen.line.gambit.components.home.Panel;
import com.andrew.verhagen.line.gambit.components.movement.MoveToPoint;
import com.andrew.verhagen.line.gambit.components.movement.MovementSpeed;
import com.andrew.verhagen.line.gambit.game.Assets;
import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;

public class ColorPanelSystem extends BaseEntitySystem {

    private final float PANEL_WIDTH = GambitGame.UI_WIDTH * 0.08f;
    private final float PANEL_HEIGHT = GambitGame.UI_HEIGHT * 0.65f;
    private final float PANEL_Y = 270f;

    private HomeEntityFactory homeEntityFactory;
    private HomeScreenInitSystem homeScreenInitSystem;
    private ComponentMapper<MoveToPoint> moveToPointComponentMapper;
    private ComponentMapper<Renderable> renderableComponentMapper;
    private ComponentMapper<MovementSpeed> movementSpeedComponentMapper;

    private MoveToPoint panelDestinationPoint;

    private int panelId;

    public ColorPanelSystem() {
        super(Aspect.all(Panel.class));
    }

    @Override
    protected void initialize() {
        createPanel();
    }

    private void createPanel() {
        panelId = homeEntityFactory.createPanel(-PANEL_WIDTH, PANEL_Y, PANEL_WIDTH, PANEL_HEIGHT, Assets.white);

        Renderable panelRenderable = renderableComponentMapper.get(panelId);
        panelRenderable.a = 0.5f;

        MovementSpeed panelSpeed = movementSpeedComponentMapper.create(panelId);
        panelSpeed.unitsPerSecond = 1000f;

        homeScreenInitSystem.createColorButtons(panelId, PANEL_WIDTH, PANEL_HEIGHT);
    }

    public void showPanel() {
        IntBag panels = this.getEntityIds();
        for (int i = 0; i < panels.size(); i++) {
            int entityId = panels.get(i);
            panelDestinationPoint = moveToPointComponentMapper.create(entityId);
            panelDestinationPoint.destinationX = 0;
            panelDestinationPoint.destinationY = PANEL_Y;
        }
    }

    public void hidePanel() {
        IntBag panels = this.getEntityIds();
        for (int i = 0; i < panels.size(); i++) {
            int entityId = panels.get(i);
            panelDestinationPoint = moveToPointComponentMapper.create(entityId);
            panelDestinationPoint.destinationX = -PANEL_WIDTH;
            panelDestinationPoint.destinationY = PANEL_Y;
        }
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected void end() {
        this.setEnabled(false);
    }
}
