package com.example.thesis.Events;

import android.util.Log;

import com.example.thesis.Coordinates.AbsoluteCoordinates;
import com.example.thesis.ProcessManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class EventReader implements Runnable {
    // TODO: comments and rename things better (for example coords and abscoords)...

    private Deque<Event> touchEvents;

    private ProcessManager processManager;

    private Process rootProcess;

    private BufferedReader bufferedReaderInput;
    private BufferedReader bufferedReaderErrors;

    private boolean doStop = false;

    public Deque<Event> getTouchEvents() {
        return touchEvents;
    }

    public EventReader() {
        this.processManager = new ProcessManager();
        this.touchEvents = new LinkedList<>();
    }


    public synchronized void doStart() {
        // TODO: destroy process and input/outpureaders.
        this.doStop = false;
        Log.i("Event Reader", "Stopped EventReader!");
    }


    public synchronized void doStop() {
        // TODO: destroy process and input/outpureaders.
        this.doStop = true;
        rootProcess.destroy();
        Log.i("Event Reader", "Stopped EventReader!");
    }


    private synchronized boolean keepRunning() {
        return !this.doStop;
    }


    @Override
    public void run() {
        Log.i("Event Reader", "Starting EventReader!");
        captureTouchEvents();
    }


    /**
     * Captures touch events and saves them as commands into the commands queue.
     * Helpful:
     * https://stackoverflow.com/questions/5711084/java-runtime-getruntime-getting-output-from-executing-a-command-line-program
     * https://stackoverflow.com/questions/18570962/permission-denied-on-rooted-android-phone-with-getevent
     */
    private void captureTouchEvents() {
        // Running command.
        rootProcess = processManager.runRootCommand("od /dev/input/event1");
        bufferedReaderInput = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
        bufferedReaderErrors = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));
        // TODO: make it read errors aswell

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


    public Queue<String> readEvent(BufferedReader bufferedReader) {
        Queue<String> eventLines = new LinkedList<>();
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


    private EventType checkEventType(Queue<String> eventLines) {
        boolean isX = false;
        boolean isY = false;

        //Checking if it is multi touch event.

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


    public Event getEvent (Queue<String> eventLines) {
        EventType eventType = checkEventType(new LinkedList(eventLines));

        if (eventType == EventType.NORMAL) {
            return getEventNORMAL(eventLines);
        } else if (eventType == EventType.MULTIPLE_TAPS) {
            return getEventMULTIPLE_TAPS(eventLines);
        } else if (eventType == EventType.DRAGGING){
            return getEventDRAGGING(eventLines);
        } else {
            // MULTI_TOUCH
            return null;
        }
    }


    public Event_Normal getEventNORMAL(Queue<String> eventLines) {
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
        return new Event_Normal(absoluteCoordinates);
    }


    public Event_Normal getEventMULTIPLE_TAPS(Queue<String> eventLines) {
        Event event = touchEvents.peek();
        Event_Normal eventCopy;

        try {
            eventCopy = (Event_Normal) event.makeCopy();
        } catch (ClassCastException e) {
            Event_Dragging event_dragging = (Event_Dragging) event;
            eventCopy = ((Event_Dragging) event).getNormalEvent();
        }

        return eventCopy;
    }


    public Event_Dragging getEventDRAGGING(Queue<String> eventLines) {
        Deque<AbsoluteCoordinates> absoluteCoordinates = new LinkedList<>();

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

        return new Event_Dragging(absoluteCoordinates);
    }
}


enum EventType {
    NORMAL,
    MULTIPLE_TAPS,
    DRAGGING,
    MULTI_TOUCH
}
