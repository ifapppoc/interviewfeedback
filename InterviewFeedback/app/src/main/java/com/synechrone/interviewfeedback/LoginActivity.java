package com.synechrone.interviewfeedback;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        //registerBroadcast();
    }

    private void authenticateUser(String username, String password) {
        Intent intent = new Intent(this, UserAuthenticationService.class);
        intent.putExtra(AppConstants.KEY_USER_NAME, username);
        intent.putExtra(AppConstants.KEY_USER_PASSWORD, password);

        startService(intent);
    }
}
