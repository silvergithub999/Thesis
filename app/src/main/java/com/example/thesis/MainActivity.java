package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    // TODO: maybe rename some of the classes to services and put them into their own folder.
    private Malware malware;
    private boolean malwareRunning = false;

    private DatabaseService database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseService(this);
        malware = new Malware(this, MainActivity.this, database);

        getAndUpdatePIN(R.id.textPIN1, 1);
        getAndUpdatePIN(R.id.textPIN2, 2);
    }


    private void getAndUpdatePIN(int textViewId, int id) {
        String PIN_string = database.getPIN(id);
        malware.updateView(PIN_string, textViewId, id);
    }


    public void startMalware(View view) {
        if (CheckRoot.hasRootAccess()) {
            if (!malwareRunning) {
                malwareRunning = true;
                Thread malwareThread = new Thread(malware, "Malware Thread");
                malwareThread.start();
                Toast.makeText(this, "Started malware!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Malware is already running!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Can't start, app has no root access!", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopMalware(View view) {
        malware.doStop();
        malwareRunning = false;
        Toast.makeText(this, "Stopped malware!", Toast.LENGTH_SHORT).show();
    }
}
