package main;

import java.util.Random;

public enum MapDirection {
    UP,
    UP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    LEFT,
    UP_LEFT;

    public Vector2d toVector(){
        switch(this) {
            case UP: return new Vector2d(0,-1);
            case UP_RIGHT: return new Vector2d(1,-1);
            case RIGHT: return new Vector2d(1,0);
            case BOTTOM_RIGHT: return new Vector2d(1,1);
            case BOTTOM: return new Vector2d(0,1);
            case BOTTOM_LEFT: return new Vector2d(-1,1);
            case LEFT: return new Vector2d(-1,0);
            case UP_LEFT: return new Vector2d(-1,-1);
        }
        return new Vector2d(0,0);
    }

    public static MapDirection getMapDirection(int i){
        return values()[i];
    }

    public MapDirection add(DirectionRotation rotation){
        return values()[(this.ordinal() + rotation.ordinal()) % values().length];
    }

    public static MapDirection getRandom() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
