package com.example.thesis.Events;

import com.example.thesis.Coordinates.AbsoluteCoordinates;

import java.util.Deque;
import java.util.LinkedList;

public abstract class Event {
    private Deque<AbsoluteCoordinates> absoluteCoordinates;

    public Event(Deque<AbsoluteCoordinates> absoluteCoordinates) {
        this.absoluteCoordinates = absoluteCoordinates;
    }

    /**
     * Meant for Event_Normal
     * @param absoluteCoordinates
     */
    public Event(AbsoluteCoordinates absoluteCoordinates) {
        Deque<AbsoluteCoordinates> deque = new LinkedList<>();
        deque.add(absoluteCoordinates);
        this.absoluteCoordinates = deque;
    }

    public Deque<AbsoluteCoordinates> getAbsoluteCoordinates() {
        return new LinkedList<>(absoluteCoordinates);
    }

    abstract public Event makeCopy();
}
