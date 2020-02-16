package com.example.thesis;

import android.util.Log;

import com.example.thesis.Coordinates.AbsoluteCoordinates;
import com.example.thesis.Coordinates.ScreenCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
    final private float widthMultiplier;
    final private float heightMultiplier;

    private ProcessManager processManager;

    private Process rootProcess;
    private BufferedReader bufferedReaderInput;
    private BufferedReader bufferedReaderErrors;

    public Converter(int screenX, int screenY) {
        processManager = new ProcessManager();

        int[] absoluteCoordinates = getAbsoluteCoordinates();
        float absoluteX = absoluteCoordinates[0];
        float absoluteY = absoluteCoordinates[1];

        widthMultiplier = screenX / absoluteX;
        heightMultiplier = screenY / absoluteY;
    }

    public ScreenCoordinates convertAbsoluteToScreenCoordinates(AbsoluteCoordinates absoluteCoordinates) {
        int screenX = Math.round(absoluteCoordinates.getAbsoluteX() * widthMultiplier);
        int screenY = Math.round(absoluteCoordinates.getAbsoluteY() * heightMultiplier);
        ScreenCoordinates screenCoordinates = new ScreenCoordinates(screenX, screenY);
        return screenCoordinates;
    }

    public Deque<ScreenCoordinates> convertAbsoluteToScreenCoordinates(Deque<AbsoluteCoordinates> absoluteCoordinates) {
        Deque<ScreenCoordinates> screenCoordinates = new LinkedList<>();
        while(!absoluteCoordinates.isEmpty()) {
            screenCoordinates.add(convertAbsoluteToScreenCoordinates(absoluteCoordinates.poll()));
        }
        return screenCoordinates;
    }


    private int[] getAbsoluteCoordinates() {
        // TODO: https://stackoverflow.com/questions/28215812/adb-shell-getevent-method-returns-twice-the-value-for-x-and-y-on-nexus-4/28217144#28217144
        try {
            rootProcess = processManager.runRootCommand("getevent -il /dev/input/event1 | grep ABS_MT_POSITION");
            bufferedReaderInput = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
            bufferedReaderErrors = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));


            Pattern pattern = Pattern.compile(", max (.+), fuzz");
            int absoluteX = -1000;
            int absoluteY = -1000;

            String line;
            while ((line = bufferedReaderInput.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    if (line.contains("ABS_MT_POSITION_X")) {
                        absoluteX = Integer.parseInt(matcher.group(1));
                    } else {
                        absoluteY = Integer.parseInt(matcher.group(1));
                        return new int[]{absoluteX, absoluteY};
                    }

                } else {
                    Log.e("Converter", "Couldn't find absolute resolution!");  // TODO: for testing, remove later.
                }
            }
            rootProcess.destroy();
            return new int[]{absoluteX, absoluteY};
        } catch (IOException error) {
            Log.e("Converter", "Error getting absolute size of screen: " + error.getMessage());
        }
        return null;
    }
}
