package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Malware malware;
    private boolean malwareRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startMalware(View view) {
        // TODO: add a check that checks for root access!

        if (!malwareRunning) {
            malwareRunning = true;
            malware = new Malware();
            Thread malwareThread = new Thread(malware, "Malware Thread");
            malwareThread.start();
            Toast.makeText(this, "Started malware!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Malware is already running!", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopMalware(View view) {
        malware.doStop();
        malwareRunning = false;
        Toast.makeText(this, "Stopped malware!", Toast.LENGTH_SHORT).show();
    }
}
