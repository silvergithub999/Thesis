package com.example.thesis;

import android.util.Log;

import com.example.thesis.Buttons.Button;
import com.example.thesis.Buttons.CancelButton;
import com.example.thesis.Buttons.DeleteButton;
import com.example.thesis.Buttons.PinButton;
import com.example.thesis.Events.Event;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * This class checks, if DisplayedScreen type is in front.
 */
public class DisplayedScreenService {
    private Process rootProcess;
    private BufferedReader bufferedReaderInput;
    private BufferedReader bufferedReaderErrors;

    private boolean hasBeenOpened = false;


    /**
     * DisplayedScreenService constructor.
     */
    public DisplayedScreenService() {
        this.rootProcess = ProcessManagerService.getRootProcess();
        this.bufferedReaderInput = new BufferedReader(new InputStreamReader(rootProcess.getInputStream()));
        this.bufferedReaderErrors = new BufferedReader(new InputStreamReader(rootProcess.getErrorStream()));
    }


    /**
     * Destroys the root process.
     */
    public void doStop() {
        rootProcess.destroy();
    }


    /**
     * Checks what app is in the foreground and converts it to DisplayedScreen.
     * @return DisplayedScreen of what is in the foreground currently.
     */
    public DisplayedScreen getCurrentScreen() {
        String foregroundApp = getAppInForeground();
        if (foregroundApp.contains("com.smart_id/com.stagnationlab.sk.TransactionActivity")) {
            hasBeenOpened = true;
            return DisplayedScreen.AUTH_PIN_1;
            // return DisplayedScreen.AUTH_PIN_2;
        } else if (hasBeenOpened && !transactionScreenRunning()) {
            transactionScreenRunning();     // TODO
            hasBeenOpened = false;
            return DisplayedScreen.AUTH_SUCCESS;
            // return DisplayedScreen.AUTH_FAILED;
        } else {
            return DisplayedScreen.OTHER;
        }
    }


    private boolean transactionScreenRunning() {
        // TODO: checks if the transaction is still running as a process.
        // Queue<String> running = ProcessManagerService.readOutput("dumpsys activity | grep \"com.smart_id/com.stagnationlab.sk.TransactionActivity\"");    // TODO: maybe grep is bugged and wont work. TEST
        Queue<String> running = ProcessManagerService.readOutput("dumpsys activity | grep \"com.smart_id\"");
        return running.size() > 0;
    }


    /**
     * Finds out what app (package) is in the foreground. Uses  "dumpsys window windows | grep "mCurrentFocus" ".
     * @return name of the app in the foreground.
     */
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


    /**
     * Finds what buttons the user pressed from touchEvents.
     * Note: also empties the touchEvents array during the process.
     * @param touchEvents - all the user touch events when the smart-id auth screen was open.
     * @return returns a Queue of integers that is the PIN.
     */
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
                        touchEvents.clear();
                        PIN.clear();
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


    /**
     * Takes the PIN Queue and sends the events to the correct button coordinates.
     * @param PIN - the PIN, which buttons to press.
     */
    public void sendPIN(Queue<Integer> PIN) {
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
