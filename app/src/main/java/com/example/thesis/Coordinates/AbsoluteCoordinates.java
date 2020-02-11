package com.example.thesis.Coordinates;

public class AbsoluteCoordinates {
    private final int absoluteX;
    private final int absoluteY;

    public AbsoluteCoordinates(int absoluteX, int absoluteY) {
        this.absoluteX = absoluteX;
        this.absoluteY = absoluteY;
    }

    public int getAbsoluteX() {
        return absoluteX;
    }

    public int getAbsoluteY() {
        return absoluteY;
    }

    @Override
    public String toString() {
        return "(" + absoluteX + ", " + absoluteY + ')';
    }
}
