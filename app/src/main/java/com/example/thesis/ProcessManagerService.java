package com.example.thesis;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * This class handles the Process class related activites.
 */
public final class ProcessManagerService {

    /**
     * Creates a process with root privileges and runs the given command.
     * @param command - the command that will run in the process.
     * @return the process that just ran the given command.
     */
    public static Process runRootCommand(String command) {
        try {
            Process process = getRootProcess();
            sendCommand(process, command);
            return process;
        } catch (Exception error) {
            Log.e("Process Manager", "Error running root command: " + error.getMessage());
        }
        return null;
    }


    /**
     * Creates a process with root privileges.
     * Helpful:
     * https://stackoverflow.com/questions/18570962/permission-denied-on-rooted-android-phone-with-getevent
     * @return a process with root privileges.
     */
    public static Process getRootProcess() {
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


    /**
     * Enters a command into the given process.
     * @param process - the process where the command will run.
     * @param command - the command you want to run in the process.
     */
    public static void sendCommand(Process process, String command) {
        try {
            OutputStream outputStream = process.getOutputStream();
            outputStream.write((command + " \n").getBytes());
            outputStream.flush();
        } catch (Exception error) {
            Log.e("Process Manager", "Error sending command with output stream: " + error.getMessage());
        }
    }


    /**
     * Creates a process with root privileges and runs the command there. And reads the output of the command.
     * Note: currently has a bug where some things don't work. For example pipe and "grep".
     * @param command - command to run in the process.
     * @return queue of the command output.
     */
    public static Queue<String> readOutput(String command) {
        Queue<String> outputLines = new ArrayDeque<>();

        Process process = getRootProcess();
        sendCommand(process, command);

        BufferedReader bufferedReaderInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader bufferedReaderErrors = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        try {
            // Reading lines.
            String line;
            while ((line = bufferedReaderInput.readLine()) != null && bufferedReaderInput.ready()) {
                outputLines.add(line);
            }
        } catch (Exception error) {
            Log.e("Process manager", "Error reading the output lines: " + error.getMessage());
        }

        process.destroy();
        return outputLines;
    }
}
