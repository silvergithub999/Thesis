package com.example.thesis;

import android.content.Context;
import android.util.Log;

import com.example.thesis.Buttons.Button;
import com.example.thesis.Buttons.CancelButton;
import com.example.thesis.Buttons.DeleteButton;
import com.example.thesis.Buttons.PinButton;
import com.example.thesis.Events.Event;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * This class checks, if Smart-ID is the app in front.
 * And what view is open in Smart-ID
 */
public class DisplayedScreenService {
    private Process rootProcess;
    private BufferedReader bufferedReaderInput;
    private BufferedReader bufferedReaderErrors;

    private ImageService imageService;


    public DisplayedScreenService(Context context) {
        this.rootProcess = ProcessManagerService.getRootProcess();
        this.bufferedReaderInput = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
        this.bufferedReaderErrors = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));  // TODO: make it read errors as well

        this.imageService = new ImageService(context);
    }


    /**
     * Destroys the root process.
     */
    public void doStop() {
        rootProcess.destroy();
    }


    public DisplayedScreen getCurrentScreen() {
        String foregroundApp = getAppInForeground();
        // if (foregroundApp.contains("com.android.calculator")) {
        // if (foregroundApp.contains("com.smart_id/com.stagnationlab.sk.TransactionActivity")) {
        if (foregroundApp.contains("com.smart_id")) {
        // TODO: find out the type and add thsoe types here.
            this.imageService.mainCode();
            return DisplayedScreen.AUTH_PIN_1;
        } else {
            return DisplayedScreen.OTHER;
        }
    }


    private String getAppInForeground() {
        try {
            ProcessManagerService.sendCommand(rootProcess, "dumpsys window windows | grep \"mCurrentFocus\"");
            return bufferedReaderInput.readLine();
        } catch (Exception error) {
            Log.e("Smart-ID Check", "Error reading the dumpsys lines: " + error.getMessage());
        }
        return null;
    }


    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        // Pin buttons.
        int[] x_list = new int[]{202, 580, 958};
        for (int i = 0, y = 1375; i <= 9; i++) {
            if (i % 3 == 0 && i != 0) {
                y += 322;
            }
            buttons.put(i, new PinButton(i + 1, 280, 280, x_list[i % 3], y));
        }
        buttons.put(0, new PinButton(0, 280, 280, x_list[1], 2341));

        // Cancel and delete buttons.
        buttons.put(-1000, new CancelButton(175, 364, 153, 2393));
        buttons.put(-500, new DeleteButton(175, 175, 972, 1130));

        return buttons;
    }


    public Queue<Integer> extractPIN(Queue<Event> touchEvents) {
        Deque<Integer> PIN = new LinkedList<>();
        Converter converter = new Converter();

        Map<Integer, Button> buttons = getButtons();
        Set<Integer> buttonValues = buttons.keySet();
        while (!touchEvents.isEmpty()) {
            Event touchEvent = touchEvents.poll();
            for (Integer value : buttonValues) {
                Button button = buttons.get(value);
                boolean isInside = button.touchInsideButton(touchEvent, converter);
                if (isInside) {
                    if (button.getValue() == -1000) {
                        // Cancel button (-1000).
                    } else if (button.getValue() == -500 && PIN.size() > 0) {
                        // Delete button (-500).
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


    public void sendPIN(Queue<Integer> PIN) {
        // TODO: maybe not the correct class for this
        Queue<Integer> PIN_copy = new LinkedList<>(PIN);
        Map<Integer, Button> buttons = getButtons();

        while (!PIN_copy.isEmpty()) {
            int pinNr = PIN_copy.poll();
            buttons.get(pinNr).touchButton();
        }
    }
}

/**
 * Holds different type of views that can exist.
 */
enum DisplayedScreen {
    AUTH_PIN_1,
    AUTH_PIN_2,
    AUTH_SUCCESS,
    AUTH_FAILED,
    OTHER,
    // AUTH_PIN_1_FAILED,
    // AUTH_PIN_2_FAILED,
}
