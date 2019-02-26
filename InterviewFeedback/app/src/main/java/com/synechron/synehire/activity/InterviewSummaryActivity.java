package com.synechron.synehire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.synechron.synehire.R;
import com.synechron.synehire.adapter.InterviewSummaryAdaptor;
import com.synechron.synehire.constants.AppConstants;
import com.synechron.synehire.exception.NoConnectivityException;
import com.synechron.synehire.utility.PrefManager;
import com.synechron.synehire.ws.APIClient;
import com.synechron.synehire.ws.APIService;
import com.synechron.synehire.ws.request.DiscussionDetails;
import com.synechron.synehire.ws.request.DiscussionDetailsSummary;
import com.synechron.synehire.ws.response.DiscussionOutcome;
import com.synechron.synehire.ws.response.InterviewSummary;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterviewSummaryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<DiscussionOutcome> outcomes;
    private List<InterviewSummary> interviewSummaries;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_summary);
        setToolbar(getString(R.string.activity_summary_title), false);
        initializeView();
        getDiscussionsOutcome();
        getInterviewSummary();
    }

    private void initializeView() {
        progressBar = findViewById(R.id.progress_circular);
        recyclerView = findViewById(R.id.recycler_view);
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDiscussionDetails();
            }
        });
    }

    private void updateDiscussionDetails() {
        progressBar.setVisibility(View.VISIBLE);
        APIService apiService = APIClient.getInstance(InterviewSummaryActivity.this);
        DiscussionDetailsSummary summary = new DiscussionDetailsSummary();
        if (interviewSummaries != null && interviewSummaries.size()> 0) {
            List<DiscussionDetails> discussionDetails = new ArrayList<>();
            DiscussionDetails discussionDetail;
            for (InterviewSummary interviewSummary : interviewSummaries) {
                discussionDetail = new DiscussionDetails();
                discussionDetail.setId(interviewSummary.getDiscussionDetailId());
                discussionDetail.setComment(interviewSummary.getComment());
                List<DiscussionOutcome> outcomes = interviewSummary.getDiscussionOutcomes();
                List<Integer> outcomeIds = new ArrayList<>();
                if (outcomes != null && outcomes.size() > 0) {
                    for (DiscussionOutcome discussionOutcome : outcomes) {
                        outcomeIds.add(discussionOutcome.getId());
                    }
                }
                discussionDetail.setOutcomesIds(outcomeIds);

                discussionDetails.add(discussionDetail);
            }

            summary.setDiscussions(discussionDetails);

            Call<JsonObject> call = apiService.updateDiscussions(summary);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.body() != null) {
                        try {
                            JsonObject json = response.body();
                            JSONObject jsonObject = new JSONObject(json.toString());
                            String status = jsonObject.getString(AppConstants.STATUS);
                            if (AppConstants.SUCCESS.equalsIgnoreCase(status)) {
                                navigateToRecommendation();
                            } else {
                                String message = jsonObject.getString(AppConstants.ERROR_MESSAGE);
                                showError(message);
                            }
                        } catch (JSONException e) {
                            showError("");
                        }
                    } else {
                        showError("");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    progressBar.setVisibility(View.GONE);
                    if (throwable instanceof NoConnectivityException) {
                        showError(throwable.getMessage());
                    } else {
                        showError("");
                    }
                }
            });
        }
    }

    private void getInterviewSummary() {
        progressBar.setVisibility(View.VISIBLE);
        long interviewId = PrefManager.getInterviewId(InterviewSummaryActivity.this);
        APIService apiService = APIClient.getInstance(InterviewSummaryActivity.this);
        Call<List<InterviewSummary>> call = apiService.getInterviewSummaries(interviewId);
        call.enqueue(new Callback<List<InterviewSummary>>() {
            @Override
            public void onResponse(Call<List<InterviewSummary>> call, Response<List<InterviewSummary>> response) {
                progressBar.setVisibility(View.GONE);
                List<InterviewSummary> interviewSummaries = response.body();
                if (interviewSummaries != null && interviewSummaries.size() > 0) {
                    updateInterviewSummaries(interviewSummaries);
                } else {
                    showError("");
                }
            }

            @Override
            public void onFailure(Call<List<InterviewSummary>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    public void getDiscussionsOutcome() {
        APIService apiService = APIClient.getInstance(InterviewSummaryActivity.this);
        Call<List<DiscussionOutcome>> call = apiService.getDiscussionsOutcome();
        call.enqueue(new Callback<List<DiscussionOutcome>>() {
            @Override
            public void onResponse(Call<List<DiscussionOutcome>> call, Response<List<DiscussionOutcome>> response) {
                List<DiscussionOutcome> outcomeList = response.body();
                if (outcomeList != null && outcomeList.size() > 0) {
                    outcomes = outcomeList;
                } else {
                    showError("");
                }
            }

            @Override
            public void onFailure(Call<List<DiscussionOutcome>> call, Throwable throwable) {
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    private void updateInterviewSummaries(List<InterviewSummary> interviewSummaries) {
        this.interviewSummaries = interviewSummaries;
        InterviewSummaryAdaptor tAdapter = new InterviewSummaryAdaptor(this, interviewSummaries, outcomes, false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(tAdapter);
    }

    private void navigateToRecommendation() {
        Intent intent = new Intent(this, RecommendationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }
}
