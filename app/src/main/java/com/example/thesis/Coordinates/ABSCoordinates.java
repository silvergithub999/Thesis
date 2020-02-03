package com.example.thesis.Coordinates;

public class ABSCoordinates {
    private final int absX;
    private final int absY;

    public ABSCoordinates(int absX, int absY) {
        this.absX = absX;
        this.absY = absY;
    }

    public int getAbsX() {
        return absX;
    }

    public int getAbsY() {
        return absY;
    }

    @Override
    public String toString() {
        return "(" + absX + ", " + absY + ')';
    }
}
