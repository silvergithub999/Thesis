package com.example.thesis.Buttons;

public class CancelButton extends Button {
    public CancelButton(int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
    }

    @Override
    public int getValue() {
        return -1000;
    }
}
