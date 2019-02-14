package com.synechrone.synehire.activity;

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

import com.crashlytics.android.Crashlytics;
import com.synechrone.synehire.R;
import com.synechrone.synehire.constants.AppConstants;
import com.synechrone.synehire.domain.EmployeeRole;
import com.synechrone.synehire.domain.UserAuthDomain;
import com.synechrone.synehire.services.UserAuthenticationService;
import com.synechrone.synehire.utility.PrefManager;
import com.synechrone.synehire.ws.APIClient;
import com.synechrone.synehire.ws.APIService;
import com.synechrone.synehire.ws.response.Employee;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputUsername;
    private TextInputLayout inputPassword;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView textViewError;
    private ProgressBar progressBarLogin;

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
    }

    private void authenticateUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        boolean isValidCredentials = validateCredentials(username, password);
        if (isValidCredentials) {
            progressBarLogin.setVisibility(View.VISIBLE);
            PrefManager.saveLoginData(this, username, password);
            Intent intent = new Intent(this, UserAuthenticationService.class);
            intent.putExtra(AppConstants.KEY_USER_NAME, username);
            intent.putExtra(AppConstants.KEY_USER_PASSWORD, password);
            startService(intent);
        }
    }

    private void getEmployeeRole(String emailId) {
        APIService apiService = APIClient.getInstance();
        Call<Employee> call = apiService.getEmployee(emailId);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                Employee employee = response.body();
                if (employee != null) {
                    navigateToNextScreen(employee);
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
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
            if (authDomain.isAuthenticated()) {
                String loginId = PrefManager.getUserId(LoginActivity.this);
                if (!loginId.isEmpty()) {
                    getEmployeeRole(loginId);
                } else {
                    getEmployeeRole(editTextUsername.getText().toString());
                }
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

    private void navigateToNextScreen(Employee employee) {
        Intent intent;
        String employeeRole = employee.getRole();
        if (EmployeeRole.RECRUITER.getRole().equalsIgnoreCase(employeeRole)) {
            intent = new Intent(this, RecruiterActionActivity.class);
        } else {
            intent = new Intent(this, CandidateDetailsActivity.class);
        }
        intent.putExtra(AppConstants.KEY_EMPLOYEE, employee);
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
