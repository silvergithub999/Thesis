package com.example.thesis.Buttons;

public class OkButton extends Button {
    public OkButton(int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
    }

    @Override
    public int getValue() {
        return 1000;
    }
}
