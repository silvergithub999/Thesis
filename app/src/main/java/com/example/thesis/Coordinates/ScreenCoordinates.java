package com.example.thesis.Coordinates;

public class ScreenCoordinates extends AbsoluteCoordinates {
    // TODO: change them to floats, since the dev tools have them as floats.
    private int x;  // TODO: x to screenX etc
    private int y;

    public ScreenCoordinates(AbsoluteCoordinates absoluteCoordinates, int x, int y) {
        super(absoluteCoordinates.getAbsoluteX(), absoluteCoordinates.getAbsoluteY());
        this.x = x;
        this.y = y;
    }

    public ScreenCoordinates(int absX, int absY, float widthMultiplier, float heightMultiplier) {
        super(absX, absY);
        this.x = x;
        this.y = y;
    }

    @Override
    public int getAbsoluteX() {
        return super.getAbsoluteX();
    }

    @Override
    public int getAbsoluteY() {
        return super.getAbsoluteY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
