package com.synechrone.interviewfeedback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.services.UserAuthenticationService;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        authenticateUser(username.getText().toString(), password.getText().toString());

        registerAuthenticationListener();
    }

    private void authenticateUser(String username, String password) {
        Intent intent = new Intent(this, UserAuthenticationService.class);
        intent.putExtra(AppConstants.KEY_USER_NAME, username);
        intent.putExtra(AppConstants.KEY_USER_PASSWORD, password);

        startService(intent);
    }

    private void registerAuthenticationListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("SOME_ACTION");
        registerReceiver(authenticationListener, filter);
    }

    BroadcastReceiver authenticationListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //do something based on the intent's action
        }
    };

    @Override
    protected void onDestroy() {
        if (authenticationListener != null) {
            unregisterReceiver(authenticationListener);
            authenticationListener = null;
        }
        super.onDestroy();
    }
}
