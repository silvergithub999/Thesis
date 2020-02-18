package com.example.thesis.Buttons;

/**
 * Class for the Smart-ID authenticating numpad ok button.
 */
public class OkButton extends Button {

    /**
     * Ok button constructor.
     * @param height - height of the button.
     * @param width - width of the button.
     * @param locationX - top-left x coordinate of the button.
     * @param locationY - top-left y coordinate of the button.
     */
    public OkButton(int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
    }

    @Override
    public int getValue() {
        return 1000;
    }
}
