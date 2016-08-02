package com.andrew.verhagen.line.gambit.systems.homescreen;

import com.andrew.verhagen.line.gambit.components.home.ManagedColor;
import com.andrew.verhagen.line.gambit.components.home.Panel;
import com.andrew.verhagen.line.gambit.components.positional.RelativePosition;
import com.andrew.verhagen.line.gambit.systems.factory.BaseEntityFactory;
import com.artemis.ComponentMapper;

public class HomeEntityFactory extends BaseEntityFactory {
    private ComponentMapper<RelativePosition> relativePositionComponentMapper;
    public static final String relativeButton = "relativeButton";
    public static final String panel = "panel";
    public static final String managedColorRenderable = "managedColorRenderable";

    @Override
    protected void createCustomArchetypes() {
        archetypeHolder.createArchetype(managedColorRenderable, BaseEntityFactory.renderable, ManagedColor.class);
        archetypeHolder.createArchetype(relativeButton, BaseEntityFactory.button, RelativePosition.class);
        archetypeHolder.createArchetype(panel, BaseEntityFactory.renderable, Panel.class);
    }

    public int createManagedColorRenderable(float x, float y, float width, float height, String textureName) {
        int managedRenderable = world.create(archetypeHolder.getArchetype(managedColorRenderable));
        super.editRenderable(managedRenderable, x, y, width, height, textureName);
        return managedRenderable;
    }

    public int createRelativeButton(float relativeX, float relativeY, float width, float height, String textureName, String eventName, int parentId) {
        int buttonId = world.create(archetypeHolder.getArchetype(relativeButton));
        super.editButton(buttonId, 0, 0, width, height, textureName, eventName);

        RelativePosition buttonRelativePosition = relativePositionComponentMapper.get(buttonId);
        buttonRelativePosition.parentId = parentId;
        buttonRelativePosition.relativeX = relativeX;
        buttonRelativePosition.relativeY = relativeY;

        return buttonId;
    }

    public int createPanel(float x, float y, float width, float height, String textureName) {
        int panelId = world.create(archetypeHolder.getArchetype(panel));
        super.editRenderable(panelId, x, y, width, height, textureName);
        return panelId;
    }
}
