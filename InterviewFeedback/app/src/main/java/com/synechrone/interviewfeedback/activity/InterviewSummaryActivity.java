package com.synechrone.interviewfeedback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.adapter.InterviewSummaryAdaptor;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.utility.PrefManager;
import com.synechrone.interviewfeedback.ws.APIClient;
import com.synechrone.interviewfeedback.ws.APIService;
import com.synechrone.interviewfeedback.ws.response.InterviewSummary;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterviewSummaryActivity extends BaseActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_summary);
        setToolbar(getString(R.string.activity_summary_title), false);
        initializeView();
        getInterviewSummary();
    }

    private void initializeView() {
        recyclerView = findViewById(R.id.recycler_view);
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRecommendation();
            }
        });
    }

    private void getInterviewSummary() {
        long interviewId = PrefManager.getInterviewId(InterviewSummaryActivity.this);
        APIService apiService = APIClient.getInstance();
        Call<List<InterviewSummary>> call = apiService.getInterviewSummaries(interviewId);
        call.enqueue(new Callback<List<InterviewSummary>>() {
            @Override
            public void onResponse(Call<List<InterviewSummary>> call, Response<List<InterviewSummary>> response) {
                List<InterviewSummary> interviewSummaries = response.body();
                if (interviewSummaries != null && interviewSummaries.size() > 0) {
                    updateInterviewSummaries(interviewSummaries);
                }
            }

            @Override
            public void onFailure(Call<List<InterviewSummary>> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private void updateInterviewSummaries(List<InterviewSummary> interviewSummaries) {
        InterviewSummaryAdaptor tAdapter = new InterviewSummaryAdaptor(interviewSummaries,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(tAdapter);
    }

    private void navigateToRecommendation() {
        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
        InterviewSummaryActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }
}
