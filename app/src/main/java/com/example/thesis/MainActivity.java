package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

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
        // TODO: allow only one malware to run.

        if (!malwareRunning) {
            malwareRunning = true;

            //Getting the screen width and height for coordinate conversion later.
            int[] resolution = getScreenSize();
            int width = resolution[0];
            int height = resolution[1];

            // Staring malware thread.
            malware = new Malware(width, height);
            Thread malwareThread = new Thread(malware);
            malwareThread.start();
        }
    }

    public void stopMalware(View view) {
        malware.stop();
        malwareRunning = false;
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
