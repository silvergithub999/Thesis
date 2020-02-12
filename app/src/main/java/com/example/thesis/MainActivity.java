package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
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

            //Getting the screen width and height for converter and creating it.
            int[] resolution = getScreenSize();
            int width = resolution[0];
            int height = resolution[1];
            Converter converter = new Converter(width, height);


            // Staring malware thread.
            malware = new Malware(converter);
            Thread malwareThread = new Thread(malware, "Malware Thread");  // TODO: name all threads
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


    public int[] getScreenSize() {
        // https://stackoverflow.com/questions/10991194/android-displaymetrics-returns-incorrect-screen-size-in-pixels-on-ics
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();

        display.getRealMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return new int[]{width, height};
    }

}
