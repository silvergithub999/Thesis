package com.example.thesis;

import android.util.Log;

import java.io.IOException;

/**
 * This class handles checking if the app has access to root so it can work.
 * TODO: Currently unfinished
 */
public final class CheckRoot {

    /**
     * Checks if the Process can access /system/bin/su.
     * @return true if it can, false if not.
     */
    public static boolean  hasRootAccess() {
        // TODO: Unfinished!
        try {
            Process rootProcess = ProcessManagerService.getRootProcess();
            return rootProcess != null;
        } catch (Exception e) {
            Log.e("CheckRoot", "Error checking root access: " + e.getMessage());
            return false;
        }

    }
}
