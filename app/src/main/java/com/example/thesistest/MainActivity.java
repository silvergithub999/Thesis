package com.example.thesistest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startMalware(View view) {
        // TODO: add a check that checks for root access!

        Log.i("MALWARE", "Stared malware!");
        SmartIDCheck smartIDCheck = new SmartIDCheck();
        smartIDCheck.run();
    }

    public void stopMalware(View view) {
        Log.i("MALWARE", "Stopped malware!");
    }

}
