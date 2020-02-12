package com.example.thesis.Events;

import com.example.thesis.Coordinates.AbsoluteCoordinates;

import java.util.Deque;

public interface Event {
    public Deque<AbsoluteCoordinates> getAbsoluteCoordinates();

    public Event makeCopy();
}
