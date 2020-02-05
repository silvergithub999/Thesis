package com.example.thesis.Coordinates;

public class ScreenCoordinates extends ABSCoordinates {
    // TODO: change them to floats, since the dev tools have them as floats.
    private int x;  // TODO: x to screenX etc
    private int y;

    public ScreenCoordinates(ABSCoordinates absCoordinates, int x, int y) {
        super(absCoordinates.getAbsX(), absCoordinates.getAbsY());
        this.x = x;
        this.y = y;
    }

    public ScreenCoordinates(int absX, int absY, float widthMultiplier, float heightMultiplier) {
        super(absX, absY);
        this.x = x;
        this.y = y;
    }

    @Override
    public int getAbsX() {
        return super.getAbsX();
    }

    @Override
    public int getAbsY() {
        return super.getAbsY();
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
