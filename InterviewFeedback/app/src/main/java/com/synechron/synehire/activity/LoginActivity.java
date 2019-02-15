package com.synechron.synehire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.synechron.synehire.R;
import com.synechron.synehire.constants.AppConstants;
import com.synechron.synehire.domain.EmployeeRole;
import com.synechron.synehire.utility.PrefManager;
import com.synechron.synehire.ws.APIClient;
import com.synechron.synehire.ws.APIService;
import com.synechron.synehire.ws.response.Employee;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputUsername;
    private TextInputLayout inputPassword;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        initializeView();
    }

    private void initializeView() {
        inputUsername = findViewById(R.id.inputLayoutUsername);
        editTextUsername = findViewById(R.id.username);
        inputPassword = findViewById(R.id.inputLayoutPassword);
        editTextPassword = findViewById(R.id.password);
        progressBarLogin = findViewById(R.id.progress_circular_login);
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
            getEmployeeRole(username);
        }
    }

    private void getEmployeeRole(String emailId) {
        APIService apiService = APIClient.getInstance();
        Call<Employee> call = apiService.getEmployee(emailId);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                progressBarLogin.setVisibility(View.GONE);
                Employee employee = response.body();
                if (employee != null) {
                    navigateToNextScreen(employee);
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable throwable) {
                progressBarLogin.setVisibility(View.GONE);
                showError();
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
            if (AppConstants.GLOBAL_USER_PASSWORD.equalsIgnoreCase(password)) {
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
        } else {
            String message = getString(R.string.error_password);
            inputPassword.setError(message);
            editTextPassword.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        return true;
    }

    private void showError() {
        inputUsername.setError("UserName is not valid!!!");
        editTextUsername.setBackgroundResource(R.drawable.edit_text_bg_error);
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
}
