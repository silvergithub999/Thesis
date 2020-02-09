package com.example.thesis;

import android.util.Log;

import com.example.thesis.Coordinates.ABSCoordinates;
import com.example.thesis.Coordinates.ScreenCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
    private float widthMultiplier;
    private float heightMultiplier;

    private ProcessManager processManager;

    public Converter(int screenX, int screenY) {
        processManager = new ProcessManager();

        int[] absoluteCoordinates = getAbsoluteCoordinates();
        float absoluteX = absoluteCoordinates[0];
        float absoluteY = absoluteCoordinates[1];

        widthMultiplier = screenX / absoluteX;
        heightMultiplier = screenY / absoluteY;
    }

    public ScreenCoordinates convertAbsToScreenCoordinates(ABSCoordinates absCoordinates) {
        int screenX = Math.round(absCoordinates.getAbsX() * widthMultiplier);
        int screenY = Math.round(absCoordinates.getAbsY() * heightMultiplier);
        ScreenCoordinates screenCoordinates = new ScreenCoordinates(absCoordinates, screenX, screenY);
        return screenCoordinates;
    }


    private int[] getAbsoluteCoordinates() {
        // TODO: https://stackoverflow.com/questions/28215812/adb-shell-getevent-method-returns-twice-the-value-for-x-and-y-on-nexus-4/28217144#28217144
        try {
            BufferedReader bufferedReader = processManager.runRootCommand("getevent -il /dev/input/event1 | grep ABS_MT_POSITION");
            Pattern pattern = Pattern.compile(", max (.+), fuzz");
            int absoluteX = -1000;
            int absoluteY = -1000;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    if (line.contains("ABS_MT_POSITION_X")) {
                        absoluteX = Integer.parseInt(matcher.group(1));
                    } else {
                        absoluteY = Integer.parseInt(matcher.group(1));
                        bufferedReader.close();
                        return new int[]{absoluteX, absoluteY};
                    }

                } else {
                    Log.e("Converter", "Couldn't find absolute resolution!");  // TODO: for testing, remove later.
                }
            }
            bufferedReader.close();
            return new int[]{absoluteX, absoluteY};
        } catch (IOException error) {
            Log.e("Converter", "Error getting absolute size of screen: " + error.getMessage());
        }
        return null;
    }
}
