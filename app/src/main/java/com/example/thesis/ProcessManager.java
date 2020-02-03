package com.example.thesis;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProcessManager {
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
            ProcessBuilder pb = new ProcessBuilder();
            pb.command("/system/bin/su");
            Process process = pb.start();

            // Sending the command.
            OutputStream outputStream = process.getOutputStream();
            outputStream.write((command + " \n").getBytes());
            outputStream.flush();

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


    /**
     * Useful when the reuslt is going to be jsut a single line.
     */
    public String getSingleLine(BufferedReader bufferedReader) {
        //TODO: close process and bufferedReader.
        return "sdap+üdlspa";
    }
}
