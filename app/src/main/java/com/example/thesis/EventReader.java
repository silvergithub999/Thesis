package com.example.thesis;

import android.util.Log;

import com.example.thesis.Coordinates.ABSCoordinates;
import com.example.thesis.Events.Event;
import com.example.thesis.Events.Event_Dragging;
import com.example.thesis.Events.Event_MultipleTaps;
import com.example.thesis.Events.Event_Normal;

import java.io.BufferedReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class EventReader implements Runnable {
    // TODO: comments and rename things better (for example coords and abscoords)...
    // TODO: multitouch events and maybe holding events.

    private ProcessManager processManager;

    private Deque<Event> touchEvents;

    private boolean doStop = false;

    public EventReader() {
        this.processManager = new ProcessManager();
        this.touchEvents = new LinkedList<>();
    }


    public synchronized void doStop() {
        // TODO: destroy process and input/outpureaders.
        this.doStop = true;
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
        BufferedReader bufferedReader = processManager.runRootCommand("od /dev/input/event1");

        // Reading result.
        String line;
        while (keepRunning()) {
            Queue<String> eventLines = readEvent(bufferedReader);
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

        ABSCoordinates absCoordinates = new ABSCoordinates(absX, absY);
        return new Event_Normal(absCoordinates);
    }


    public Event_MultipleTaps getEventMULTIPLE_TAPS(Queue<String> eventLines) {
        Deque<ABSCoordinates> touchEventABSCoordinates = new LinkedList<>();
        Event event = touchEvents.removeLast();
        Deque<ABSCoordinates> absCoordinates = event.getAbsCoordinates();
        absCoordinates.add(absCoordinates.peek());
        Event_MultipleTaps event_multipleTaps = new Event_MultipleTaps(absCoordinates);
        return event_multipleTaps;
    }


    public Event_Dragging getEventDRAGGING(Queue<String> eventLines) {
        Deque<ABSCoordinates> absCoordinates = new LinkedList<>();

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
                absCoordinates.add(new ABSCoordinates(absX, absY));
                isX = false;
                isY = false;
            }
        }

        return new Event_Dragging(absCoordinates);
    }
}


enum EventType {
    NORMAL,
    MULTIPLE_TAPS,
    DRAGGING,
    MULTI_TOUCH
}
