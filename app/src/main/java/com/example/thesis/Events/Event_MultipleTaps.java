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

    public void tap() {
        if (absCoordinates.size() != 0) {
            // TODO: check that not the same object, if it is then copy of object
            absCoordinates.add(absCoordinates.getLast());
        }
    }

    @Override
    public String toString() {
        return "Multiple Tap Event: " + absCoordinates;
    }
}
