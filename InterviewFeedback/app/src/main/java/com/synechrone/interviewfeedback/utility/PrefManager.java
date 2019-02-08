package com.synechrone.interviewfeedback.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

public class PrefManager {
    Context context;
    SharedPreferences sharedpreferences;
    private static final String SYNEHIRE_PREFS_NAME = "syneHIRE";
    private static final String PREF_KEY_INTERVIEW_ID = "interviewId";
    private static final String PREF_KEY_LEVEL_ID = "levelId";
    private static final String PREF_KEY_DISCUSSION_DETAILS_ID = "discussionDetailId";

    public PrefManager(Context context){
        this.context=context;
    }

    public void saveLoginData(String loginId, String loginPassword){
        sharedpreferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();
        editor.putString("loginId", loginId);
        editor.putString("loginPassword", loginPassword);
        editor.commit();

    }

    public String getUserId(){
        return context.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("loginId","");
    }

    public String getUserPassword(){
        return context.getSharedPreferences("loginData", Context.MODE_PRIVATE).getString("loginPassword","");
    }

    public boolean isVerified(){
        return context.getSharedPreferences("VerificationData", Context.MODE_PRIVATE).getBoolean("isVerified",false);
    }

    public void saveVerificationData(boolean isVerified){
        sharedpreferences = context.getSharedPreferences("VerificationData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();
        editor.putBoolean("isVerified", isVerified);
        editor.commit();
    }

    public void saveKeywordsForFeedback(List keywords,int position){
        sharedpreferences = context.getSharedPreferences("KeywordsForFeedback"+position, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();
        Gson gson = new Gson();
        String jsonText = gson.toJson(keywords);
        editor.putString("Keywords", jsonText);
        editor.commit();
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

    public static void saveDiscussionDetailsId(Context context, long discussionDetailId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(PREF_KEY_DISCUSSION_DETAILS_ID, discussionDetailId);
        editor.apply();
    }

    public static void saveInterviewLevelId(Context context, int levelId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(PREF_KEY_LEVEL_ID, levelId);
        editor.apply();
    }

    public static int getInterviewLevelId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SYNEHIRE_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(PREF_KEY_LEVEL_ID, 0);
    }
}
