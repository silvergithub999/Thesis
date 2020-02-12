package com.example.thesis;

import android.util.Log;

import java.io.OutputStream;

public class ProcessManager {
    public Process runRootCommand(String command) {
        try {
            // Starting process as su.
            Process process = getRootProcess();


            // Sending the command.
            OutputStream outputStream = process.getOutputStream();
            runRootCommand(outputStream, command);

            return process;
        } catch (Exception error) {
            Log.e("Process Manager", "Error running root command: " + error.getMessage());
        }
        return null;
    }


    public Process getRootProcess() {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("/system/bin/su");
            Process process = pb.start();
            return process;
        } catch (Exception error) {
            Log.e("Process Manager", "Error running root process: " + error.getMessage());
        }
        return null;
    }


    public void runRootCommand(OutputStream outputStream, String command) {
        try {
            outputStream.write((command + " \n").getBytes());
            outputStream.flush();
        } catch (Exception error) {
            Log.e("Process Manager", "Error sending command with output stream: " + error.getMessage());
        }
    }
}
