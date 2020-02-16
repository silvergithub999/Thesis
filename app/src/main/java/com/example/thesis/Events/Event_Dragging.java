package com.example.thesis.Events;

import com.example.thesis.Coordinates.AbsoluteCoordinates;

import java.util.Deque;
import java.util.LinkedList;

public class Event_Dragging extends Event {
    public Event_Dragging(Deque<AbsoluteCoordinates> absoluteCoordinates) {
        super(absoluteCoordinates);
    }

    @Override
    public String toString() {
        return "Dragging Event: " + super.getAbsoluteCoordinates();
    }


    @Override
    public Event makeCopy() {
        return new Event_Dragging(super.getAbsoluteCoordinates());
    }


    /**
     * This for the case, where the following touch is in the same spot dragging ended.
     * @return Event_Normal made of the last recorded coordinates.
     */
    public Event_Normal getNormalEvent() {
        Deque<AbsoluteCoordinates> deque = new LinkedList<>();
        deque.add(super.getAbsoluteCoordinates().getLast());
        return new Event_Normal(deque);
    }
}
