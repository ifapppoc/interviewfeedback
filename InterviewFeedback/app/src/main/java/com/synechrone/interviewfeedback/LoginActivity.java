package com.synechrone.interviewfeedback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        authenticateUser(username.getText().toString(), password.getText().toString());
    }

    private void authenticateUser(String username, String password) {

    }
}
