package com.synechrone.synehire.logger;

import android.util.Log;

import com.synechrone.synehire.constants.AppConstants;

public class AppLogger {
    public static void logError(String message) {
        Log.e(AppConstants.LOG_TAG, message);
    }
}
