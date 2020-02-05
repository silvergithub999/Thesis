package com.example.thesis;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProcessManager {
    // TODO: maybe move bufferedReaders into the classes themselves and not here, keep only processes here.

    private List<BufferedReader> allBufferedReadersInput;
    private List<BufferedReader> allBufferedReadersErrors;
    private List<Process> allProcesses;

    public ProcessManager() {
        this.allBufferedReadersInput = new ArrayList<>();
        this.allBufferedReadersErrors = new ArrayList<>();
        this.allProcesses = new ArrayList<>();
    }

    public void stopAll() {
        for (BufferedReader bufferedReader : allBufferedReadersInput) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                Log.e("Process Manager", "Failed to close BufferedReader: " + e.getMessage());
            }
        }
        for (BufferedReader bufferedReader : allBufferedReadersErrors) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                Log.e("Process Manager", "Failed to close BufferedReader: " + e.getMessage());
            }
        }
        for (Process process : allProcesses) {
            process.destroy();
        }
    }

    public BufferedReader runRootCommand(String command) {
        try {
            // Starting process as su.
            Process process = getRootProcess();


            // Sending the command.
            OutputStream outputStream = process.getOutputStream();
            runRootCommand(outputStream, command);

            // Buffered readers of outputs and error outputs.
            BufferedReader bufferedReaderInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader bufferedReaderErrors = new BufferedReader(new InputStreamReader(process.getErrorStream()));  // TODO: make it read errors aswell

            allBufferedReadersInput.add(bufferedReaderInput);
            allBufferedReadersErrors.add(bufferedReaderErrors);
            allProcesses.add(process);

            return bufferedReaderInput;
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
            allProcesses.add(process);
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
