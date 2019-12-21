package main;

import java.util.Random;

public enum DirectionRotation {
    ROT0,
    ROT45,
    ROT90,
    ROT135,
    ROT180,
    ROT225,
    ROT270,
    ROT315;

    public int getNumber() {
        return ordinal();
    }

    public static DirectionRotation getDirectionRotation(int i){
        return values()[i];
    }

    public static DirectionRotation getRandom() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
