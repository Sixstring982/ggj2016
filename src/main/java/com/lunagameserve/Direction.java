package com.lunagameserve;

/**
 * Created by sixstring982 on 1/30/16.
 */
public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Direction opposite() {
        switch (this) {
            case NORTH: return SOUTH;
            case EAST: return WEST;
            case SOUTH: return NORTH;
            case WEST: return EAST;
            default: throw new IllegalStateException();
        }
    }
}
