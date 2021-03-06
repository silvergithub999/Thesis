package com.example.thesis.Events;

import android.util.Log;

import com.example.thesis.Coordinates.AbsoluteCoordinates;
import com.example.thesis.ProcessManagerService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;


/**
 * This class handels user touch events.
 */
public class EventReader implements Runnable {
    private Deque<Event> touchEvents;
    private Process rootProcess;
    private boolean doStop = false;

    /**
     * Class constructor.
     */
    public EventReader() {
        this.touchEvents = new ArrayDeque<>();
    }


    /**
     * Returns all touch events so far recorder.
     * @return queue of user touch Events.
     */
    public Queue<Event> getTouchEvents() {
        return touchEvents;
    }


    /**
     * Stops the eventreader thread and destroys the Process listening.
     */
    public synchronized void doStop() {
        this.doStop = true;
        if (rootProcess != null) {
            rootProcess.destroy();
        }
        Log.i("Event Reader", "Stopped EventReader!");
    }


    /**
     * Restarts the EventReader thread when doStop has been called.
     */
    public synchronized void doStart() {
        this.doStop = false;
        Log.i("Event Reader", "Restarted EventReader!");
    }


    /**
     * Checks if doStop has been called and if the thread needs to stop or run.
     * @return boolean if true the thread keeps running, else the main loop stops.
     */
    private synchronized boolean keepRunning() {
        return !this.doStop;
    }


    @Override
    public void run() {
        Log.i("Event Reader", "Starting EventReader!");
        captureTouchEvents();
    }


    /**
     * Captures touch events from "/dev/input/event1"
     * and saves them as Events into the touchEvents queue.
     */
    private void captureTouchEvents() {
        // Running command.
        rootProcess = ProcessManagerService.runRootCommand("od /dev/input/event1");
        BufferedReader bufferedReaderInput = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
        BufferedReader bufferedReaderErrors = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));

        // Reading result.
        String line;
        while (keepRunning()) {
            Queue<String> eventLines = readEvent(bufferedReaderInput);
            Event event =  eventLines.size() > 3 ? getEvent(eventLines) : null;
            if (event != null) {
                touchEvents.add(event);
                Log.i("Event Reader", "Captured touch: " + event);
            } else {
                Log.i("Event Reader", "Ignoring multi touch event!");
            }
        }
    }


    /**
     * Reads an event lines until the end of event line appears, then returns the queue of lines captured.
     * @param bufferedReader - process inputstream bufferedreader where the captured events are sent.
     * @return queue of lines that are captured from "/dev/input/event1".
     */
    private Queue<String> readEvent(BufferedReader bufferedReader) {
        Queue<String> eventLines = new ArrayDeque<>();
        String endOfEvent = "000003  000071  177777  177777";
        try {
            // Reading lines.
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(endOfEvent)) {
                    eventLines.add(line);
                    eventLines.add(bufferedReader.readLine());
                    break;
                }
                eventLines.add(line);
            }
        } catch (Exception error) {
            Log.e("Event Reader", "Error reading the event lines: " + error.getMessage());
        }
        return eventLines;
    }


    /**
     * Checks what type of event was captured.
     * @param eventLines - lines captured from readEvent.
     * @return the EventType of the event that happened.
     */
    private EventType checkEventType(Queue<String> eventLines) {
        boolean isX = false;
        boolean isY = false;

        // Checking if there are x and y coordinates.
        while(!eventLines.isEmpty()) {
            String line = eventLines.poll();
            String[] lineSplit = line.split("  ");

            if (lineSplit[6].equals("000065")) {
                isX = true;
            } else if (lineSplit[6].equals("000066")) {
                isY = true;
                break;
            }
        }

        // Checking if there are more x and y coordinates - indicating dragging event.
        while(!eventLines.isEmpty()) {
            String line = eventLines.poll();
            String[] lineSplit = line.split("  ");

            if (lineSplit[6].equals("000065") || lineSplit[6].equals("000066")) {
                return EventType.DRAGGING;
            } else if (lineSplit[6].equals("000057")) {
                // Multi touch event -> ABS_MT_SLOT = 000057
                return EventType.MULTI_TOUCH;

            }
        }

        // Returing what type of event it was by which coordinates were present.
        if (isX && isY) {
            return EventType.NORMAL;
        } else {
            return EventType.MULTIPLE_TAPS;
        }
    }


    /**
     * Get the Event class object from eventlines captured from readEvent.
     * @param eventLines - lines captured from readEvent.
     * @return class Event object of the event that happened.
     */
    private Event getEvent (Queue<String> eventLines) {
        EventType eventType = checkEventType(new ArrayDeque(eventLines));
        if (eventType == EventType.NORMAL) {
            return getEventNORMAL(eventLines);
        } else if (eventType == EventType.MULTIPLE_TAPS) {
            return getEventMULTIPLE_TAPS(eventLines);
        } else if (eventType == EventType.DRAGGING){
            return getEventDRAGGING(eventLines);
        } else {
            // MULTI_TOUCH - not supported. And
            return null;
        }
    }


    /**
     * If a normal event happened then this function creates the Event class from eventlines
     * from readEvent.
     * @param eventLines - lines captured from readEvent.
     * @return class Event object of the normal touch event that happened.
     */
    private Event getEventNORMAL(Queue<String> eventLines) {
        int absX = -1000, absY = -1000;
        while(!eventLines.isEmpty()) {
            String line = eventLines.poll();
            String[] lineSplit = line.split("  ");

            if (lineSplit[6].equals("000065")) {
                // ABS_MT_POSITION_X == 000065
                absX = Integer.parseInt(lineSplit[7], 8);
            } else if (lineSplit[6].equals("000066")) {
                // ABS_MT_POSITION_Y == 000066
                absY = Integer.parseInt(lineSplit[7], 8);
            }
        }
        AbsoluteCoordinates absoluteCoordinates = new AbsoluteCoordinates(absX, absY);
        return new Event(absoluteCoordinates);
    }


    /**
     * If an event happened, where user touched multiple times in the same place,
     * then this function creates the Event class from eventlines from readEvent.
     * @param eventLines - lines captured from readEvent.
     * @return class Event object of the multiple taps event that happened.
     */
    private Event getEventMULTIPLE_TAPS(Queue<String> eventLines) {
        Event event = touchEvents.getLast();
        return event.getLastEvent();
    }


    /**
     * If an event happened, where the user dragged their finger,
     * then this function creates the Event class from eventlines from readEvent.
     * @param eventLines - lines captured from readEvent.
     * @return class Event object of the dragging event that happened.
     */
    private Event getEventDRAGGING(Queue<String> eventLines) {
        // TODO: fix bug where if started dragging where previous touch was, then there are no coordinates.
        Deque<AbsoluteCoordinates> absoluteCoordinates = new ArrayDeque<>();

        int absX = -1000, absY = -1000;
        boolean isX = false, isY = false;

        while(!eventLines.isEmpty()) {
            String line = eventLines.poll();
            String[] lineSplit = line.split("  ");

            if (lineSplit[6].equals("000065")) {
                // ABS_MT_POSITION_X == 000065
                absX = Integer.parseInt(lineSplit[7], 8);
                isX = true;
            } else if (lineSplit[6].equals("000066")) {
                // ABS_MT_POSITION_Y == 000066
                absY = Integer.parseInt(lineSplit[7], 8);
                isY = true;
            } else if (
                    lineSplit[5].equals("000000") &&
                            lineSplit[6].equals("000000") &&
                            lineSplit[7].equals("000000") &&
                            lineSplit[8].equals("000000") &&
                            (isX || isY)) {
                // SYN_REPORT == ... 000000  000000  000000  000000 <- indicates end of this set of coordinates
                absoluteCoordinates.add(new AbsoluteCoordinates(absX, absY));
                isX = false;
                isY = false;
            }
        }
        return new Event(absoluteCoordinates);
    }
}


/**
 * This holds all the types of events that can happend in the device.
 */
enum EventType {
    NORMAL,
    MULTIPLE_TAPS,
    DRAGGING,
    MULTI_TOUCH
}
