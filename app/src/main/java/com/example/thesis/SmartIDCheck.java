package com.example.thesis;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * This class checks, if Smart-ID is the app in front.
 */
public class SmartIDCheck {
    private ProcessManager processManager;

    private Process rootProcess;
    private OutputStream outputStream;
    private BufferedReader bufferedReaderInput;
    private BufferedReader bufferedReaderErrors;


    public SmartIDCheck() {
        this.processManager = new ProcessManager();
        this.rootProcess = processManager.getRootProcess();
        this.outputStream = rootProcess.getOutputStream();

        this.bufferedReaderInput = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
        this.bufferedReaderErrors = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));  // TODO: make it read errors aswell
    }


    /**
     * Checks whether the Smart-ID app is in front or not.
     * @return true if Smart-ID is in front, false if not.
     * // TODO: for testing, currently is calculator.
     */
    public boolean isSmartIDInForeground() {
        // TODO: maybe make it smarter, so it won't work on demo or other screen other than entering PINs?

        String foregroundApp = getAppInForeground();

        // if (foregroundApp.contains("smart_id")) {
        if (foregroundApp.contains("com.android.calculator")) { // TODO: This is for testing, remove later!
            return true;
        }
        return false;
    }



    private String getAppInForeground() {
        // https://stackoverflow.com/questions/28543776/android-shell-get-foreground-app-package-name
        try {
            this.processManager.runRootCommand(outputStream, "dumpsys window windows | grep \"mCurrentFocus\"");
            return bufferedReaderInput.readLine();
        } catch (Exception error) {
            Log.e("Smart-ID Check", "Error reading the dumpsys lines: " + error.getMessage());
        }
        return null;
    }
}
