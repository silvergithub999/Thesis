package com.example.thesis.Events;

import com.example.thesis.Coordinates.ABSCoordinates;

import java.util.Deque;

public class Event_Dragging implements Event {
    private Deque<ABSCoordinates> absCoordinates;

    public Event_Dragging(Deque<ABSCoordinates> absCoordinates) {
        this.absCoordinates = absCoordinates;
    }

    public Deque<ABSCoordinates> getAbsCoordinates() {
        return absCoordinates;
    }

    @Override
    public void sendEvent() {

    }

    @Override
    public String toString() {
        return "Dragging Event: " + absCoordinates;
    }

    @Override
    public Event makeCopy() {
        return new Event_Dragging(getAbsCoordinates());
    }
}
