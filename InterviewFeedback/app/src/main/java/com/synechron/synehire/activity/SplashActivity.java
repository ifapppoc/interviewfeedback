package com.synechrone.synehire.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.synechrone.synehire.R;
import com.synechrone.synehire.constants.AppConstants;
import com.synechrone.synehire.domain.EmployeeRole;
import com.synechrone.synehire.domain.UserAuthDomain;
import com.synechrone.synehire.services.UserAuthenticationService;
import com.synechrone.synehire.utility.PrefManager;
import com.synechrone.synehire.ws.APIClient;
import com.synechrone.synehire.ws.APIService;
import com.synechrone.synehire.ws.response.Employee;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private ProgressBar progressBarSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBarSplash = findViewById(R.id.progress_circular_splash);
        registerAuthenticationListener();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                handleAuthentication();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void handleAuthentication() {
        progressBarSplash.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, UserAuthenticationService.class);
        intent.putExtra(AppConstants.KEY_USER_NAME, PrefManager.getUserId(SplashActivity.this));
        intent.putExtra(AppConstants.KEY_USER_PASSWORD, PrefManager.getUserPassword(SplashActivity.this));
        startService(intent);
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

    private void registerAuthenticationListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.KEY_LOGIN_BROADCAST_ACTION);
        registerReceiver(authenticationListener, filter);
    }

    BroadcastReceiver authenticationListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBarSplash.setVisibility(View.GONE);
            UserAuthDomain authDomain = (UserAuthDomain)intent.getSerializableExtra(AppConstants.KEY_AUTH_RESPONSE);
            if (authDomain.isAuthenticated()) {
                String loginId = PrefManager.getUserId(SplashActivity.this);
                if (!loginId.isEmpty()) {
                    getEmployeeRole(loginId);
                } else {
                    navigateToLoginScreen();
                }
            } else {
                navigateToLoginScreen();
            }
        }
    };

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

    @Override
    protected void onDestroy() {
        if (authenticationListener != null) {
            unregisterReceiver(authenticationListener);
            authenticationListener = null;
        }
        super.onDestroy();
    }
}
