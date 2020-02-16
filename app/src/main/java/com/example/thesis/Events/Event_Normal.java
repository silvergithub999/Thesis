package com.example.thesis.Events;

import com.example.thesis.Coordinates.AbsoluteCoordinates;

import java.util.Deque;
import java.util.LinkedList;

public class Event_Normal extends Event {
    public Event_Normal(Deque<AbsoluteCoordinates> absoluteCoordinates) {
        super(absoluteCoordinates);
    }

    public Event_Normal(AbsoluteCoordinates absoluteCoordinates) {
        super(absoluteCoordinates);
    }

    @Override
    public String toString() {
        return "Normal Event: " + super.getAbsoluteCoordinates();
    }


    @Override
    public Event makeCopy() {
        return new Event_Normal(super.getAbsoluteCoordinates());
    }
}
