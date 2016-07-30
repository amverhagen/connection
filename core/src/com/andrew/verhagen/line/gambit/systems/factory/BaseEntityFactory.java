package com.andrew.verhagen.line.gambit.systems.factory;

import com.artemis.BaseSystem;

public class BaseEntityFactory extends BaseSystem {

    private static final String boundedBox = "bounded_box";

    public BaseEntityFactory() {
        this.setEnabled(false);
    }

    @Override
    protected void processSystem() {

    }
}
