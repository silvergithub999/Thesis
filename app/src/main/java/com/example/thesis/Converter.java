package com.example.thesis;

import com.example.thesis.Coordinates.AbsoluteCoordinates;
import com.example.thesis.Coordinates.ScreenCoordinates;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class handles the conversion of absolute coordinates to screen coordinates.
 */
public class Converter {
    final private float widthMultiplier;
    final private float heightMultiplier;


    /**
     * Constructor for the converter class.
     * @param screenX - screen max x coordinates, is the screen resolution x value.
     * @param screenY - screen max y coordinates, is the screen resolution y value.
     */
    public Converter(int screenX, int screenY) {
        AbsoluteCoordinates absoluteCoordinates = getAbsoluteCoordinates();
        float absoluteX = absoluteCoordinates.getAbsoluteX();
        float absoluteY = absoluteCoordinates.getAbsoluteY();

        widthMultiplier = screenX / absoluteX;
        heightMultiplier = screenY / absoluteY;
    }


    /**
     * Converts a absolute coordinates to screen coordinates.
     * @param absoluteCoordinates - the coordinates to convert.
     * @return converted ScreenCoordinates.
     */
    public ScreenCoordinates convertAbsoluteToScreenCoordinates(AbsoluteCoordinates absoluteCoordinates) {
        int screenX = Math.round(absoluteCoordinates.getAbsoluteX() * widthMultiplier);
        int screenY = Math.round(absoluteCoordinates.getAbsoluteY() * heightMultiplier);
        ScreenCoordinates screenCoordinates = new ScreenCoordinates(screenX, screenY);
        return screenCoordinates;
    }


    /**
     * Converts a deque of absolute coordinates to a deque of screen coordinates.
     * Important to note it empties the absolute coordinates deque.
     * @param absoluteCoordinates - the coordinates to convert.
     * @return deque of converted ScreenCoordinates.
     */
    public Deque<ScreenCoordinates> convertAbsoluteToScreenCoordinates(Deque<AbsoluteCoordinates> absoluteCoordinates) {
        Deque<ScreenCoordinates> screenCoordinates = new LinkedList<>();
        while(!absoluteCoordinates.isEmpty()) {
            screenCoordinates.add(convertAbsoluteToScreenCoordinates(absoluteCoordinates.poll()));
        }
        return screenCoordinates;
    }


    /**
     * Gets the max absolute coordinates of the screen.
     * Need for conversions form absolute coordinates to screen coordinates.
     * @return AbsoluteCoordinates class with the max x and y.
     */
    private AbsoluteCoordinates getAbsoluteCoordinates() {
        ProcessManagerService processManagerService = new ProcessManagerService();
        Queue<String> outputLines = processManagerService.readOutput("getevent -il /dev/input/event1");

        Pattern pattern = Pattern.compile(", max (.+), fuzz");
        int absoluteX = -1000;
        int absoluteY = -1000;

        while (!outputLines.isEmpty()) {
            String line = outputLines.poll();
            if (line.contains("ABS_MT_POSITION")) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    if (line.contains("ABS_MT_POSITION_X")) {
                        absoluteX = Integer.parseInt(matcher.group(1));
                    } else {
                        absoluteY = Integer.parseInt(matcher.group(1));
                        return new AbsoluteCoordinates(absoluteX, absoluteY);
                    }
                }
            }
        }

        return null;
    }
}
