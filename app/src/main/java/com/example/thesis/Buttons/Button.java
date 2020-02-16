package com.example.thesis.Buttons;

import com.example.thesis.Converter;
import com.example.thesis.Coordinates.AbsoluteCoordinates;
import com.example.thesis.Coordinates.ScreenCoordinates;
import com.example.thesis.Events.Event;

import java.util.Deque;
import java.util.LinkedList;

public abstract class Button {
    private int height;
    private int width;
    private int locationX;
    private int locationY;

    public Button(int height, int width, int locationX, int locationY) {
        this.height = height;
        this.width = width;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public boolean coordinatesInsideButton(ScreenCoordinates screenCoordinates) {
        if (screenCoordinates.getScreenX() >= locationX &&
        screenCoordinates.getScreenX() <= locationX + width &&
        screenCoordinates.getScreenY() >= locationY &&
        screenCoordinates.getScreenY() <= locationY + height) {
            return true;
        }
        return false;
    }


    public boolean touchInsideButton(Event event, Converter converter) {
        // TODO
        Deque<AbsoluteCoordinates> absoluteCoordinates = event.getAbsoluteCoordinates();
        Deque<ScreenCoordinates> screenCoordinates = converter.convertAbsoluteToScreenCoordinates(new LinkedList(absoluteCoordinates));

        while (!screenCoordinates.isEmpty()) {
            ScreenCoordinates coords = screenCoordinates.poll();
            boolean isInside = coordinatesInsideButton(coords);
            if (!isInside) {
                return false;
            }
        }
        return true;
    }

    public abstract int getValue();
}
