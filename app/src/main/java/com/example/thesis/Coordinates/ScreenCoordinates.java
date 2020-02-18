package com.example.thesis.Coordinates;

/**
 * Class to hold the screen coordinates.
 */
public class ScreenCoordinates {
    private final int screenX;
    private final int screenY;

    /**
     * Screen coordinates constructor.
     * @param screenX - x coordinate.
     * @param screenY - y coordinate.
     */
    public ScreenCoordinates(int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }

    /**
     * Returns the x coordinate of the coordinates.
     * @return - x coordinate.
     */
    public int getScreenX() {
        return screenX;
    }

    /**
     * Returns the y coordinate of the coordinates.
     * @return - y coordinate.
     */
    public int getScreenY() {
        return screenY;
    }

    @Override
    public String toString() {
        return "(" + screenX + ", " + screenY + ')';
    }
}
