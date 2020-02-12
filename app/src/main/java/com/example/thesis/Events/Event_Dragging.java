package com.example.thesis.Events;

import com.example.thesis.Coordinates.AbsoluteCoordinates;

import java.util.Deque;

public class Event_Dragging implements Event {
    private Deque<AbsoluteCoordinates> absoluteCoordinates;

    public Event_Dragging(Deque<AbsoluteCoordinates> absoluteCoordinates) {
        this.absoluteCoordinates = absoluteCoordinates;
    }

    public Deque<AbsoluteCoordinates> getAbsoluteCoordinates() {
        return absoluteCoordinates;
    }


    @Override
    public String toString() {
        return "Dragging Event: " + absoluteCoordinates;
    }

    @Override
    public Event makeCopy() {
        return new Event_Dragging(getAbsoluteCoordinates());
    }
}
