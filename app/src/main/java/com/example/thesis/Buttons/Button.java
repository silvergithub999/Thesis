package com.example.thesis.Buttons;

import com.example.thesis.Converter;
import com.example.thesis.Coordinates.ScreenCoordinates;
import com.example.thesis.Events.Event;

import java.util.Deque;

/**
 * Class to extend for cancel, ok and PIN buttons.
 */
public abstract class Button {
    final private int height;
    final private int width;
    final private int locationX;
    final private int locationY;

    /**
     * Button constructor.
     * @param height - height of the button.
     * @param width - width of the button.
     * @param locationX - top-left x coordinate of the button.
     * @param locationY - top-left y coordinate of the button.
     */
    public Button(int height, int width, int locationX, int locationY) {
        this.height = height;
        this.width = width;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    /**
     * Checks if the coordinates are inside the button.
     * @param screenCoordinates - coordinates of the touch.
     * @return true if touch the button was touched, else false.
     */
    public boolean coordinatesInsideButton(ScreenCoordinates screenCoordinates) {
        return screenCoordinates.getScreenX() >= locationX &&
        screenCoordinates.getScreenX() <= locationX + width &&
        screenCoordinates.getScreenY() >= locationY &&
        screenCoordinates.getScreenY() <= locationY + height;
    }


    /**
     * Checks if the event happened inside the button.
     * @param event - touch event.
     * @param converter - converter to convert the event absolute coordinates to screen coordinates.
     * @return true if the button was touched, false if not.
     */
    public boolean touchInsideButton(Event event, Converter converter) {
        Deque<ScreenCoordinates> screenCoordinates = event.getScreenCoordinates(converter);

        while (!screenCoordinates.isEmpty()) {
            ScreenCoordinates coords = screenCoordinates.poll();
            boolean isInside = coordinatesInsideButton(coords);
            if (!isInside) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the value of the button.
     * @return numpad (0-9), cancel (-1000), ok (1000).
     */
    public abstract int getValue();
}
