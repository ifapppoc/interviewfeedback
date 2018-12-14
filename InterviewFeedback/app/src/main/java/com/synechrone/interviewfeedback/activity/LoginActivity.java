package com.synechrone.interviewfeedback.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.UserAuthDomain;
import com.synechrone.interviewfeedback.services.UserAuthenticationService;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView textViewError;
    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        progressBarLogin = findViewById(R.id.progress_circular_login);
        textViewError = findViewById(R.id.textViewError);
        Button loginButton = findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

        registerAuthenticationListener();
    }

    private void authenticateUser() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean isValidCredentials = validateCredentials(username, password);
        if (isValidCredentials) {
            progressBarLogin.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, UserAuthenticationService.class);
            intent.putExtra(AppConstants.KEY_USER_NAME, username);
            intent.putExtra(AppConstants.KEY_USER_PASSWORD, password);
            startService(intent);
        }
    }

    private boolean validateCredentials(String username, String password) {
        return false;
    }

    private void registerAuthenticationListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.KEY_LOGIN_BROADCAST_ACTION);
        registerReceiver(authenticationListener, filter);
    }

    BroadcastReceiver authenticationListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBarLogin.setVisibility(View.GONE);
            UserAuthDomain authDomain = (UserAuthDomain)intent.getSerializableExtra(AppConstants.KEY_AUTH_RESPONSE);
            Log.i("MY_APP", "Success: "+ authDomain.isAuthenticated());
            if (authDomain.isAuthenticated()) {
                navigateToNextScreen();
            } else {
                showError(authDomain);
            }
        }
    };

    private void showError(UserAuthDomain authDomain) {
        String error = authDomain.getErrorMessage();
        if (!error.isEmpty()) {
            textViewError.setVisibility(View.VISIBLE);
            textViewError.setText(error);
        }
    }

    private void navigateToNextScreen() {
    }

    @Override
    protected void onDestroy() {
        if (authenticationListener != null) {
            unregisterReceiver(authenticationListener);
            authenticationListener = null;
        }
        super.onDestroy();
    }
}
