package com.example.thesis.Events;

import com.example.thesis.Converter;
import com.example.thesis.Coordinates.AbsoluteCoordinates;
import com.example.thesis.Coordinates.ScreenCoordinates;
import com.example.thesis.ProcessManager;

public class EventSender {
    final private Converter converter;
    final private ProcessManager processManager;

    public EventSender(Converter converter) {
        this.converter = converter;
        this.processManager = new ProcessManager();
    }


    public void sendEvent(ScreenCoordinates screenCoordinates) {
        StringBuilder command = new StringBuilder();
        command.append("input tap ");
        command.append(screenCoordinates.getScreenX());
        command.append(" ");
        command.append(screenCoordinates.getScreenY());

        Process process = processManager.runRootCommand(command.toString());
        process.destroy();
    }


    public void sendEvent(AbsoluteCoordinates absoluteCoordinates) {
        ScreenCoordinates screenCoordinates = converter.convertAbsoluteToScreenCoordinates(absoluteCoordinates);
        sendEvent(screenCoordinates);
    }
}
