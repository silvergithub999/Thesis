package com.example.thesis.Events;

import com.example.thesis.Coordinates.ABSCoordinates;

public class Event_Normal implements Event {
    private ABSCoordinates absCoordinates;

    public Event_Normal(ABSCoordinates absCoordinates) {
        this.absCoordinates = absCoordinates;
    }

    public ABSCoordinates getAbsCoordinates() {
        return absCoordinates;
    }

    @Override
    public void sendEvent() {

    }

    @Override
    public String toString() {
        return "Normal Event: " + absCoordinates;
    }
}
