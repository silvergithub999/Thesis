package com.example.thesis.Events;

import com.example.thesis.Coordinates.ABSCoordinates;

import java.util.Deque;

public interface Event {
    public void sendEvent();

    public Deque<ABSCoordinates> getAbsCoordinates();
}
