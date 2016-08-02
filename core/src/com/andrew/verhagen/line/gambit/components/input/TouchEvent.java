package com.andrew.verhagen.line.gambit.components.input;

import com.artemis.World;

public abstract class TouchEvent {
    public abstract boolean touched(World world, int id, float touchX, float touchY);
}
