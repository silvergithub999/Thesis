package com.example.thesis.Buttons;

/**
 * Class for the Smart-ID numpad delete button.
 * When clicked removes last digit of entered PIN.
 */
public class DeleteButton extends Button {
    /**
     * Delete button constructor.
     * @param height - height of the button.
     * @param width - width of the button.
     * @param locationX - top-left x coordinate of the button.
     * @param locationY - top-left y coordinate of the button.
     */
    public DeleteButton(int height, int width, int locationX, int locationY) {
        super(height, width, locationX, locationY);
    }

    @Override
    public int getValue() {
        return -500;
    }
}
