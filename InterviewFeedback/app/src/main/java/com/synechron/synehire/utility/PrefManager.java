package com.synechrone.synehire.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String SYNEHIRE_PREFS_NAME = "syneHIRE";
    private static final String PREF_KEY_LOGIN_ID = "loginId";
    private static final String PREF_KEY_PASSWORD = "loginPassword";
    private static final String PREF_KEY_INTERVIEW_ID = "interviewId";
    private static final String PREF_KEY_LEVEL_ID = "levelId";

    public static void saveLoginData(Context context, String loginId, String loginPassword){
        SharedPreferences sharedpreferences = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PREF_KEY_LOGIN_ID, loginId);
        editor.putString(PREF_KEY_PASSWORD, loginPassword);
        editor.apply();
    }

    public static String getUserId(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PREF_KEY_LOGIN_ID, "");
    }

    public static String getUserPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(PREF_KEY_PASSWORD, "");
    }

    public static void saveInterviewId(Context context, long interviewId){
        SharedPreferences.Editor editor = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(PREF_KEY_INTERVIEW_ID, interviewId);
        editor.apply();
    }

    public static long getInterviewId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getLong(PREF_KEY_INTERVIEW_ID, 0);
    }

    public static void saveInterviewLevelId(Context context, int levelId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(PREF_KEY_LEVEL_ID, levelId);
        editor.apply();
    }

    public static int getInterviewLevelId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(PREF_KEY_LEVEL_ID, 0);
    }

    public static void logout(Context context) {
        SharedPreferences myPrefs = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.commit();
    }
}
