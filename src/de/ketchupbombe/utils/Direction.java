package de.ketchupbombe.utils;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public enum Direction {
    RIGHT(1),
    LEFT(-1),
    UP(-9),
    DOWN(9),
    RIGHT_UP(-8),
    RIGHT_DOWN(10),
    LEFT_UP(-10),
    LEFT_DOWN(8);

    private int slot;

    Direction(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
