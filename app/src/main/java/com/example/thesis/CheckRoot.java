package com.example.thesis;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * This class handles checking if the app has access to root so it can work.
 * TODO: Currently unfinished
 */
public class CheckRoot {
    private Context context;

    public CheckRoot(Context context) {
        this.context = context;
    }

    /**
     * Checks if the Process can access /system/bin/su file.
     * @return true if it can, false if not.
     */
    public boolean suFileExists() {
        try {
            Process rootProcess = ProcessManagerService.getRootProcess();
            if (rootProcess != null) {
                rootProcess.destroy();
                return true;
            }
            return false;
        } catch (Exception e) {
            Toast.makeText(context, "Can't find su file!", Toast.LENGTH_SHORT).show();
            Log.e("CheckRoot", "Error checking su file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extra check if the root access is there.
     * @return true if it, false if it is not.
     * // TODO: improve code, currently just checks if can create converter object.
     */
    public boolean hasRootAccess() {
        try {
            Converter converter = new Converter();
            return true;
        } catch (Exception e) {
            Toast.makeText(context, "No root permissions!", Toast.LENGTH_SHORT).show();
            Log.e("CheckRoot", "Error checking root access: " + e.getMessage());
            return false;
        }
    }
}
