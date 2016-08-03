package com.andrew.verhagen.line.gambit.components.player;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction getLeft(Direction currentDirection) {
        if (currentDirection == UP)
            return LEFT;
        if (currentDirection == RIGHT)
            return UP;
        if (currentDirection == DOWN)
            return RIGHT;
        if (currentDirection == LEFT)
            return DOWN;
        else return null;
    }

    public static Direction getRight(Direction currentDirection) {
        if (currentDirection == UP)
            return RIGHT;
        if (currentDirection == RIGHT)
            return DOWN;
        if (currentDirection == DOWN)
            return LEFT;
        if (currentDirection == LEFT)
            return UP;
        else return null;
    }
}