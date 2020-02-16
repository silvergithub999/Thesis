package com.example.thesis;

import android.util.Log;

import com.example.thesis.Buttons.Button;
import com.example.thesis.Buttons.CancelButton;
import com.example.thesis.Buttons.OkButton;
import com.example.thesis.Buttons.PinButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class checks, if Smart-ID is the app in front.
 */
// TODO: rename class
public class SmartIDCheck {
    private ProcessManager processManager;

    private Process rootProcess;
    private BufferedReader bufferedReaderInput;
    private BufferedReader bufferedReaderErrors;


    public SmartIDCheck() {
        this.processManager = new ProcessManager();
        this.rootProcess = processManager.getRootProcess();

        this.bufferedReaderInput = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
        this.bufferedReaderErrors = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));  // TODO: make it read errors aswell
    }


    /**
     * Destroys the root process.
     */
    public void doStop() {
        rootProcess.destroy();
    }


    /**
     * Checks whether the Smart-ID app is in front or not.
     * @return true if Smart-ID is in front, false if not.
     * // TODO: for testing currently is calculator.
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
            this.processManager.runRootCommand(rootProcess.getOutputStream(), "dumpsys window windows | grep \"mCurrentFocus\"");
            return bufferedReaderInput.readLine();
        } catch (Exception error) {
            Log.e("Smart-ID Check", "Error reading the dumpsys lines: " + error.getMessage());
        }
        return null;
    }

    public List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        // Pin buttons.
        int[] x_list = new int[]{202, 580, 958};
        for (int i = 1, x = 0, y = 1375; i <= 9; i++) {
            if (i % 3 == 0) {
                y += 322;
            }

            Button pinButton = new PinButton(i, 280, 280, x_list[x], y);
            buttons.add(pinButton);
        }
        Button pinButton = new PinButton(0, 280, 280, 580, 2341);
        buttons.add(pinButton);

        // Cancel and OK buttons.
        Button cancelButton = new CancelButton(175, 364, 153, 2393);
        Button okButton = new OkButton(175, 364, 923, 2393);
        buttons.add(cancelButton);
        buttons.add(okButton);

        return buttons;
    }


    public boolean successAuth() {
        return true;
    }
}
