package com.synechron.synehire.logger;

import android.util.Log;

import com.synechron.synehire.constants.AppConstants;

public class AppLogger {
    public static void logError(String message) {
        Log.e(AppConstants.LOG_TAG, message);
    }
}
