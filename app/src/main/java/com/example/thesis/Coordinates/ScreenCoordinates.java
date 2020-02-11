package com.example.thesis.Coordinates;

public class ScreenCoordinates {
    // TODO: change them to floats, since the dev tools have them as floats.
    private final int screenX;  // TODO: screenX to screenX etc
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
