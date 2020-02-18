package com.example.thesis.Buttons;

/**
 * Class for the Smart-ID authenticating numpad digit button.
 */
public class PinButton extends Button{
    private int value;

    /**
     * Numpad button constructor.
     * @param value - number of the numpad button.
     * @param height - height of the button.
     * @param width - width of the button.
     * @param locationX - top-left x coordinate of the button.
     * @param locationY - top-left y coordinate of the button.
     */
    public PinButton(int value, int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}
