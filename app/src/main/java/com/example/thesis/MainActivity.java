package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Malware malware;
    private boolean malwareRunning = false;

    private DatabaseService database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseService(this);
        malware = new Malware(this, MainActivity.this, database);

        // Updating the PIN numbers on screen.

        malware.updateView(malware.getPIN1(), R.id.valuePIN1);
        malware.updateView(malware.getPIN2(), R.id.valuePIN2);
    }


    /**
     * Checks if has root access and then starts the Malware class thread.
     * @param view - view of app, since the function is called when the "START MALWARE" button is pressed.
     */
    public void startMalware(View view) {
        if (CheckRoot.hasRootAccess()) {
            if (!malwareRunning) {
                malwareRunning = true;
                malware = new Malware(this, MainActivity.this, database);
                Thread malwareThread = new Thread(malware, "Malware Thread");
                malwareThread.start();
                changeIsMalwareRunningText("Malware is running.");
                Toast.makeText(this, "Started malware!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Malware is already running!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Can't start, app has no root access!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Stops the Malware class thread.
     * @param view - view of app, since the function is called when the "STOP MALWARE" button is pressed.
     */
    public void stopMalware(View view) {
        // TODO: crashes when Smart-ID is running and the malware is stopped.
        if (malwareRunning) {
            malware.doStop();
            malwareRunning = false;
            changeIsMalwareRunningText("Malware is not running.");
            Toast.makeText(this, "Stopped malware!", Toast.LENGTH_SHORT).show();
        }
    }


    public void changeIsMalwareRunningText(String text) {
        TextView isMalwareRunning = this.findViewById(R.id.isMalwareRunning);
        isMalwareRunning.setText(text);
    }
}
