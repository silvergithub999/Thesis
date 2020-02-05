package com.example.thesis.Events;

import com.example.thesis.Coordinates.ABSCoordinates;

import java.util.Deque;

public class Event_MultipleTaps implements Event {
    private Deque<ABSCoordinates> absCoordinates;

    public Event_MultipleTaps(Deque<ABSCoordinates> absCoordinates) {
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
        return "Multiple Tap Event: " + absCoordinates;
    }
}
