package com.example.thesis.Buttons;

public class PinButton extends Button{
    private int value;

    public PinButton(int value, int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
