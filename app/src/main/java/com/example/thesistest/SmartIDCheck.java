package com.example.thesistest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class checks, if Smart-ID is the app in front.
 */
public class SmartIDCheck implements Runnable {
    private boolean running = false;


    /**
     * Starts the thread, that checks every second if the Smart-ID app is in front.
     */
    public void run() {
        this.running = true;

        while(running) {
            Log.i("SMART-ID CHECK THREAD", runConsoleCommand("getprop"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.i("SMART-ID CHECK THREAD", "sleep interrupted!");
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
     * @param app - String from "dumpsys activity | grep "mFocusedActivity:"" command.
     * @return true if Smart-ID is in front, false if not.
     */
    private boolean isSmartID(String app) {
        // TODO: maybe make it smarter, so it won't work on demo?
        if (app.contains("smart_id")) {
            return true;
        }
        return false;
    }


    /**
     * Checks what app is in the foreground.
     * @return result of the command "dumpsys activity | grep "mFocusedActivity:".
     */
    private String checkForegroundApp() {
        return runConsoleCommand("dumpsys activity | grep \"mFocusedActivity:\"");
    }


    /**
     * Runs console commands.
     * @param command - the command you want to run.
     * @return string of the command result.
     */
    private String runConsoleCommand(String command) {
        // https://stackoverflow.com/questions/11255568/how-to-read-output-of-android-process-command
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
