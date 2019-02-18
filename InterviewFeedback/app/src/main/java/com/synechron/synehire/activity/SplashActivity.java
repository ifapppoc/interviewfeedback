package com.synechron.synehire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.synechron.synehire.R;
import com.synechron.synehire.constants.AppConstants;
import com.synechron.synehire.domain.EmployeeRole;
import com.synechron.synehire.exception.NoConnectivityException;
import com.synechron.synehire.utility.PrefManager;
import com.synechron.synehire.ws.APIClient;
import com.synechron.synehire.ws.APIService;
import com.synechron.synehire.ws.response.Employee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private ProgressBar progressBarSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBarSplash = findViewById(R.id.progress_circular_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                handleAuthentication();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void handleAuthentication() {
        progressBarSplash.setVisibility(View.VISIBLE);
        String loginId = PrefManager.getUserId(SplashActivity.this);
        if (!loginId.isEmpty()) {
            getEmployeeRole(loginId);
        } else {
            navigateToLoginScreen();
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
        SplashActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }

    private void navigateToLoginScreen() {
        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }

    private void getEmployeeRole(String emailId) {
        APIService apiService = APIClient.getInstance(SplashActivity.this);
        Call<Employee> call = apiService.getEmployee(emailId);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                progressBarSplash.setVisibility(View.GONE);
                Employee employee = response.body();
                if (employee != null) {
                    navigateToNextScreen(employee);
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable throwable) {
                progressBarSplash.setVisibility(View.GONE);
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }
}
