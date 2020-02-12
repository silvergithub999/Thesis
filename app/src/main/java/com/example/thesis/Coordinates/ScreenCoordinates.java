package com.example.thesis.Coordinates;

public class ScreenCoordinates {
    private final int screenX;
    private final int screenY;

    public ScreenCoordinates(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    @Override
    public String toString() {
        return "(" + screenX + ", " + screenY + ')';
    }
}
