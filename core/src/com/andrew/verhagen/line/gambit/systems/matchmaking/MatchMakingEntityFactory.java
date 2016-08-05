package com.andrew.verhagen.line.gambit.systems.matchmaking;

import com.andrew.verhagen.line.gambit.components.home.ManagedColor;
import com.andrew.verhagen.line.gambit.gameutils.Assets;
import com.andrew.verhagen.line.gambit.systems.factory.BaseEntityFactory;
import com.artemis.ComponentMapper;

public class MatchMakingEntityFactory extends BaseEntityFactory {

    private ComponentMapper<ManagedColor> managedColorComponentMapper;

    @Override
    protected void createCustomArchetypes() {

    }

    protected void createConnectionButton(float x, float y, float width, float height) {
        int connectionButton = createButton(x, y, width, height, Assets.clear, null);
        managedColorComponentMapper.create(connectionButton);
    }
}
