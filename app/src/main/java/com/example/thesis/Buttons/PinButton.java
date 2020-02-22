package com.example.thesis.Buttons;

/**
 * Class for the Smart-ID authenticating numpad digit button.
 */
public class PinButton extends Button{
    private ButtonValue value;

    /**
     * Numpad button constructor.
     * @param value - buttonValue of the pin numpad button: ZERO, ONE, ..., NINE
     * @param height - height of the button.
     * @param width - width of the button.
     * @param locationX - top-left x coordinate of the button.
     * @param locationY - top-left y coordinate of the button.
     */
    public PinButton(ButtonValue value, int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
        this.value = value;
    }

    /**
     * Numpad button constructor.
     * @param value - int value of the pin button.
     * @param height - height of the button.
     * @param width - width of the button.
     * @param locationX - top-left x coordinate of the button.
     * @param locationY - top-left y coordinate of the button.
     */
    public PinButton(int value, int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
        this.value = ButtonValueConverter.convertPinIntToButtonValue(value);
    }

    @Override
    public ButtonValue getValue() {
        return this.value;
    }
}
