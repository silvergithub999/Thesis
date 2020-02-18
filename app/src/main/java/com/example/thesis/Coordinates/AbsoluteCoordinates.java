package com.example.thesis.Coordinates;

/**
 * Class to hold the absolute coordinates.
 * Absolute coordinates are
 */
public class AbsoluteCoordinates {
    private final int absoluteX;
    private final int absoluteY;

    /**
     * Absolute coordinates constructor.
     * @param absoluteX - x coordinate.
     * @param absoluteY - y coordinate.
     */
    public AbsoluteCoordinates(int absoluteX, int absoluteY) {
        this.absoluteX = absoluteX;
        this.absoluteY = absoluteY;
    }

    /**
     * Returns the x coordinate of the coordinates.
     * @return - x coordinate.
     */
    public int getAbsoluteX() {
        return absoluteX;
    }

    /**
     * Returns the y coordinate of the coordinates.
     * @return - y coordinate.
     */
    public int getAbsoluteY() {
        return absoluteY;
    }

    @Override
    public String toString() {
        return "(" + absoluteX + ", " + absoluteY + ')';
    }
}
