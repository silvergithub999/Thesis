package com.example.thesis;

import android.util.Log;

import java.io.IOException;

public final class CheckRoot {
    public static boolean  hasRootAccess() {
        Process process = ProcessManagerService.getRootProcess();
        // TODO
        try {
            // TODO: process.getErrorStream().read();
            return true;
        } catch (Exception e) {
            Log.e("CheckRoot", "Error checking root access");
        }
        return false;
    }
}
