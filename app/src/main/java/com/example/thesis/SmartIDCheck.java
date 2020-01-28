package com.example.thesis;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class checks, if Smart-ID is the app in front.
 */
public class SmartIDCheck implements Runnable {
    private boolean running = false;
    private boolean isSmartID = false;

    public boolean isSmartID() {
        return isSmartID;
    }

    /**
     * Starts the thread, that checks every second if the Smart-ID app is in front.
     */
    public void run() {
        // TODO: maybe timer https://stackoverflow.com/questions/1453295/timer-timertask-versus-thread-sleep-in-java ?
        this.running = true;

        while(running) {
            if (smartIDInForeground()) {
                Log.i("Smart-ID Check", "Smart-ID in foreground!");
                this.isSmartID = true;
            } else {
                this.isSmartID = false;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.i("Smart-ID Check", "Run loop interrupted!");
            }
        }
    }


    /**
     * Stops this thread.
     */
    public void stop() {
        this.running = false;
    }


    /**
     * Checks whether the Smart-ID app is in front or not.
     * @return true if Smart-ID is in front, false if not.
     */
    private boolean smartIDInForeground() {
        // TODO: maybe make it smarter, so it won't work on demo or other screen other than entering PINs?

        String foregroundApp = runConsoleCommand("dumpsys activity | grep \"mFocusedActivity:\"");
        // if (foregroundApp.contains("smart_id")) {
        if (foregroundApp.contains("com.android.calculator")) { // TODO: This is for testing, remove later!
            return true;
        }
        return false;
    }


    /**
     * Runs console commands.
     * Helped by: https://stackoverflow.com/questions/11255568/how-to-read-output-of-android-process-command
     * @param command - the command you want to run.
     * @return string of the command result.
     */
    private String runConsoleCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line + "\n");
            }
            Log.i("App in front", log.toString());
            return log.toString();
        } catch (IOException error) {
            Log.e("Smart-ID Check", error.getMessage());
            return "";
        }
    }
}
