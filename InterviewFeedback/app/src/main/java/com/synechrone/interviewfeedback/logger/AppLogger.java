package com.synechrone.interviewfeedback.logger;

import android.util.Log;

import com.synechrone.interviewfeedback.constants.AppConstants;

public class AppLogger {

    public static void logError(String message) {
        Log.e(AppConstants.LOG_TAG, message);
    }
}
