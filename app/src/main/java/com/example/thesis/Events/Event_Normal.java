package com.example.thesis.Events;

import com.example.thesis.Coordinates.ABSCoordinates;

import java.util.Deque;
import java.util.LinkedList;

public class Event_Normal implements Event {
    private ABSCoordinates absCoordinates;

    public Event_Normal(ABSCoordinates absCoordinates) {
        this.absCoordinates = absCoordinates;
    }

    public Deque<ABSCoordinates> getAbsCoordinates() {
        Deque<ABSCoordinates> deque = new LinkedList<>();
        deque.add(absCoordinates);
        return deque;
    }

    @Override
    public void sendEvent() {

    }

    @Override
    public String toString() {
        return "Normal Event: " + absCoordinates;
    }
}
