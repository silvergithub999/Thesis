package com.example.thesis.Events;

import com.example.thesis.Converter;
import com.example.thesis.Coordinates.AbsoluteCoordinates;
import com.example.thesis.Coordinates.ScreenCoordinates;

import java.util.Deque;
import java.util.LinkedList;

/**
 * The Event class holds coordinate of an event.
 * Can be a single touch event, multiple taps event or dragging event.
 */
public class Event {
    private Deque<AbsoluteCoordinates> absoluteCoordinates;

    public Event(Deque<AbsoluteCoordinates> absoluteCoordinates) {
        this.absoluteCoordinates = absoluteCoordinates;
    }

    public Event(AbsoluteCoordinates absoluteCoordinates) {
        Deque<AbsoluteCoordinates> deque = new LinkedList<>();
        deque.add(absoluteCoordinates);
        this.absoluteCoordinates = deque;
    }

    /**
     * Returns absoluteCoordinates deque.
     * @return copy of absoluteCoordinates.
     */
    public Deque<AbsoluteCoordinates> getAbsoluteCoordinates() {
        return new LinkedList<>(absoluteCoordinates);
    }

    /**
     * Returns Event with the last set of coordinates. Meant for multiple tap events.
     * @return Event made with the last coordinates of the current event.
     */
    public Event getLastEvent() {
        return new Event(absoluteCoordinates.getLast());
    }

    /**
     * Converts absoluteCoordinates to screen coordinates.
     * @param converter - converter, which will do the converting.
     * @return deque of screen coordinates of the event.
     */
    public Deque<ScreenCoordinates> getScreenCoordinates(Converter converter) {
        return converter.convertAbsoluteToScreenCoordinates(new LinkedList(absoluteCoordinates));
    }

    @Override
    public String toString() {
        return absoluteCoordinates.toString();
    }
}
