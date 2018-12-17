package com.synechrone.interviewfeedback.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.UserAuthDomain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationService extends IntentService {

    private static final String JSON_FILE_PATH = "appdata/logindata/logindata.json";

    private Map<String, String> credMap = null;

    public UserAuthenticationService()
    {
        super("UserAuthenticationService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
      Bundle extraValues = intent.getExtras();
      UserAuthDomain authDomain = null;
      if(extraValues != null)
      {
          String userName = extraValues.getString(AppConstants.KEY_USER_NAME);
          String password = extraValues.getString(AppConstants.KEY_USER_PASSWORD);
          authDomain = authenticateUser(userName,password);
          Intent authIntent = new Intent();
          authIntent.setAction(AppConstants.KEY_LOGIN_BROADCAST_ACTION);
          authIntent.putExtra(AppConstants.KEY_AUTH_RESPONSE, authDomain);
          sendBroadcast(authIntent);
      }
    }


    public UserAuthDomain authenticateUser(String userName, String password)
    {
        UserAuthDomain authDomain = new UserAuthDomain();
        boolean authenticated = false;
        String errorMessage = "";
        int errorCode = 0;
        if(credMap == null || credMap.size() == 0 ) {
            credMap = createMap(loadJSONFromAsset());
        }
        if(userName == null || userName.equals("") || password.equals("") || password == null)
        {
            authenticated = false;
            errorMessage = "Username/Password can not be null !!!";
            errorCode = 3;
        }
        else
        {
            if(!credMap.containsKey(userName))
            {
                authenticated = false;
                errorMessage = "UserName is not valid!!!";
                errorCode = 1;
            }
            else if(!(credMap.get(userName).equals(password)))
            {
                authenticated = false;
                errorMessage = "Password is not valid!!!";
                errorCode = 2;
            }
            else
            {
                authenticated = true;
                errorMessage = "Logged In Successfully !!!";
            }
        }
        authDomain.setAuthenticated(authenticated);
        authDomain.setErrorMessage(errorMessage);
        authDomain.setErrorCode(errorCode);
        return authDomain;
    }

    public String loadJSONFromAsset() {
        String jsonString = null;
        try {
            InputStream inputStream = getApplicationContext().getAssets().open(JSON_FILE_PATH);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public Map<String, String> createMap(String jsonData)
    {
        credMap = new HashMap<String, String>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("credentials");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String password = jsonObj.getString(AppConstants.KEY_USER_PASSWORD);
                String userName = jsonObj.getString(AppConstants.KEY_USER_NAME);
                credMap.put(userName, password);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return credMap;
    }
}
