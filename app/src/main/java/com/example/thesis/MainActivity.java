package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private Malware malware;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startMalware(View view) {
        // TODO: add a check that checks for root access!
        // TODO: allow only one malware to run.


        //Getting the screen width and height for coordinate conversion later.
        // https://stackoverflow.com/questions/4743116/get-screen-width-and-height-in-android
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        getScreenSize();

        // Staring malware thread.
        malware = new Malware(width, height);
        Thread malwareThread = new Thread(malware);
        malwareThread.start();


    }

    public void stopMalware(View view) {
        malware.stop();
    }


    public String getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();

        display.getRealMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Log.i("Height", String.valueOf(height));
        Log.i("Width", String.valueOf(width));

        return null;
    }

}
