package com.example.thesis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Malware malware;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        malware = new Malware();
    }


    public void startMalware(View view) {
        // TODO: add a check that checks for root access!
        // TODO: allow only one malware to run.

        // Staring malware thread.
        Thread malwareThread = new Thread(malware);
        malwareThread.start();

        //...
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        Log.i("location x", String.valueOf(x));
        Log.i("location y", String.valueOf(y));
    }

    public void stopMalware(View view) {
        malware.stop();
    }

}
