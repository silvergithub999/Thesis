package com.example.thesis.Events;

import com.example.thesis.ABSCoordinates;

import java.util.Deque;

public class Event_MultipleTaps implements Event {
    private Deque<ABSCoordinates> absCoordinates;

    public Deque<ABSCoordinates> getAbsCoordinates() {
        return absCoordinates;
    }

    @Override
    public void sendEvent() {

    }
}
