package com.synechrone.interviewfeedback.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PrefManager {
    Context context;

    public PrefManager(Context context){
        this.context=context;
    }
    SharedPreferences sharedpreferences;

    public void saveloginData(String loginId,String loginPassword){
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
    public List getKeywordsForFeedback(int position){
        sharedpreferences = context.getSharedPreferences("KeywordsForFeedback"+position, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        List keywords = gson.fromJson(sharedpreferences.getString("Keywords",""), ArrayList.class);
        return keywords;
    }

}
