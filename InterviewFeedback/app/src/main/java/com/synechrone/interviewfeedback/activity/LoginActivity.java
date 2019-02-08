package com.synechrone.interviewfeedback.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import com.synechrone.interviewfeedback.utility.PrefManager;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputUsername;
    private TextInputLayout inputPassword;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView textViewError;
    private ProgressBar progressBarLogin;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        initializeView();
        registerAuthenticationListener();
    }

    private void initializeView() {
        inputUsername = findViewById(R.id.inputLayoutUsername);
        editTextUsername = findViewById(R.id.username);
        inputPassword = findViewById(R.id.inputLayoutPassword);
        editTextPassword = findViewById(R.id.password);
        progressBarLogin = findViewById(R.id.progress_circular_login);
        textViewError = findViewById(R.id.textViewError);
        Button loginButton = findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });
        prefManager=new PrefManager(this);
        if ( prefManager.isVerified()) {
            Log.d("58Login",Boolean.toString(prefManager.isVerified()));
            Log.d("59LoginUserPassword",prefManager.getUserId()+" "+prefManager.getUserPassword());
            progressBarLogin.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, UserAuthenticationService.class);
            intent.putExtra(AppConstants.KEY_USER_NAME, prefManager.getUserId());
            intent.putExtra(AppConstants.KEY_USER_PASSWORD, prefManager.getUserPassword());
            startService(intent);
        }

    }

    private void authenticateUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        prefManager.saveLoginData(username,password);
        boolean isValidCredentials=false;
        isValidCredentials = validateCredentials(username, password);
        //code change
        if (isValidCredentials || prefManager.isVerified()) {
            Log.d("67Login",Boolean.toString(prefManager.isVerified()));
            prefManager.saveVerificationData(isValidCredentials);
            progressBarLogin.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, UserAuthenticationService.class);
            intent.putExtra(AppConstants.KEY_USER_NAME, username);
            intent.putExtra(AppConstants.KEY_USER_PASSWORD, password);
            startService(intent);

        }
    }

    private boolean validateCredentials(String username, String password) {
        if (username != null && !username.isEmpty()) {
            inputUsername.setError(null);
            editTextUsername.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputUsername.setErrorEnabled(false);
            editTextUsername.clearFocus();
        } else {
            String message = getString(R.string.error_username);
            inputUsername.setError(message);
            editTextUsername.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        if (password != null && !password.isEmpty()) {
            inputPassword.setError(null);
            editTextPassword.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputPassword.setErrorEnabled(false);
            editTextPassword.clearFocus();
        } else {
            String message = getString(R.string.error_password);
            inputPassword.setError(message);
            editTextPassword.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        return true;
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
        switch (authDomain.getErrorCode()) {
            case 1: {
                inputUsername.setError(error);
                editTextUsername.setBackgroundResource(R.drawable.edit_text_bg_error);
            }
            break;
            case 2: {
                inputPassword.setError(error);
                editTextPassword.setBackgroundResource(R.drawable.edit_text_bg_error);
            }
            break;
            case 3: {
                textViewError.setVisibility(View.VISIBLE);
                textViewError.setText(error);
            }
            break;
        }
    }

    private void navigateToNextScreen() {
        Intent intent = new Intent(this, CandidateDetailsActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
