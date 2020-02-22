package com.example.thesis.Buttons;

/**
 * Class for the Smart-ID authenticating numpad cancel button.
 */
public class CancelButton extends Button {
    /**
     * Cancel button constructor.
     * @param height - height of the button.
     * @param width - width of the button.
     * @param locationX - top-left x coordinate of the button.
     * @param locationY - top-left y coordinate of the button.
     */
    public CancelButton(int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
    }

    @Override
    public ButtonValue getValue() {
        return ButtonValue.CANCEL;
    }
}
