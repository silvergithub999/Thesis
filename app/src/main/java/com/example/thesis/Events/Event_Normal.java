package com.example.thesis.Events;

import com.example.thesis.Coordinates.AbsoluteCoordinates;
import com.example.thesis.ProcessManager;

import java.util.Deque;
import java.util.LinkedList;

public class Event_Normal implements Event {
    private AbsoluteCoordinates absoluteCoordinates;

    public Event_Normal(AbsoluteCoordinates absoluteCoordinates) {
        this.absoluteCoordinates = absoluteCoordinates;
    }

    public Deque<AbsoluteCoordinates> getAbsoluteCoordinates() {
        Deque<AbsoluteCoordinates> deque = new LinkedList<>();
        deque.add(absoluteCoordinates);
        return deque;
    }


    @Override
    public String toString() {
        return "Normal Event: " + absoluteCoordinates;
    }

    @Override
    public Event makeCopy() {
        return new Event_Normal(getAbsoluteCoordinates().peek());
    }
}
