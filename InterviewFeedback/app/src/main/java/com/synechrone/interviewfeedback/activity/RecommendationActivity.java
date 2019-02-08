package com.synechrone.interviewfeedback.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.adapter.RecommendationAdapter;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.RecommendationRow;
import com.synechrone.interviewfeedback.utility.PrefManager;
import com.synechrone.interviewfeedback.ws.APIClient;
import com.synechrone.interviewfeedback.ws.APIService;
import com.synechrone.interviewfeedback.ws.request.InterviewRecommendation;
import com.synechrone.interviewfeedback.ws.request.RecommendationDetails;
import com.synechrone.interviewfeedback.ws.response.Recommendation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationActivity extends BaseActivity {

    private RecyclerView recyclerViewRecommendation;
    private RecommendationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        setToolbar(getString(R.string.activity_recommendation_title), true);
        initializeView();
        int levelId = 1;//PrefManager.getInterviewLevelId(RecommendationActivity.this);
        getRecommendations(levelId);
    }

    private void initializeView() {
        recyclerViewRecommendation = findViewById(R.id.recycler_view_recommendation);
        Button buttonSubmitAssessment = findViewById(R.id.submit_assessment);
        buttonSubmitAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RecommendationRow> recommendationRows = adapter.getRecommendations();
                submitInterviewSummary();
                submitInterviewRecommendations(recommendationRows);
            }
        });
    }

    private void getRecommendations(int levelId) {
        APIService apiService = APIClient.getInstance();
        Call<List<Recommendation>> call = apiService.getRecommendations(levelId);
        call.enqueue(new Callback<List<Recommendation>>() {
            @Override
            public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
                List<Recommendation> recommendations = response.body();
                if (recommendations != null && recommendations.size() > 0) {
                    updateRecommendation(recommendations);
                }
            }

            @Override
            public void onFailure(Call<List<Recommendation>> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private void updateRecommendation(List<Recommendation> recommendations) {
        List<RecommendationRow> recommendationRows = new ArrayList<>();
        for (Recommendation recommendation : recommendations) {
            if (recommendation != null) {
                recommendationRows.add(new RecommendationRow(recommendation.getId(), recommendation.getRecommendation()));
            }
        }
        adapter = new RecommendationAdapter(this, recommendationRows);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRecommendation.setLayoutManager(mLayoutManager);
        recyclerViewRecommendation.setAdapter(adapter);
    }

    private void submitInterviewSummary() {
        APIService apiService = APIClient.getInstance();
        long interviewId = PrefManager.getInterviewId(RecommendationActivity.this);
        Call<Boolean> call = apiService.saveInterviewSummary(interviewId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean isSubmitted = response.body();
                if (isSubmitted) {
                    //TODO: Interview Submitted Successfully
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private void submitInterviewRecommendations(List<RecommendationRow> recommendationRows) {
        APIService apiService = APIClient.getInstance();
        long interviewId = PrefManager.getInterviewId(RecommendationActivity.this);
        InterviewRecommendation interviewRecommendation = new InterviewRecommendation();
        interviewRecommendation.setInterviewId(interviewId);
        enrichWithInterviewRecommendation(interviewRecommendation, recommendationRows);
        Call<Boolean> call = apiService.saveInterviewRecommendations(interviewRecommendation);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean isSubmitted = response.body();
                if (isSubmitted) {
                    //TODO: Interview Recommendation Submitted Successfully
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private InterviewRecommendation enrichWithInterviewRecommendation(InterviewRecommendation interviewRecommendation, List<RecommendationRow> recommendationRows) {
        if (recommendationRows != null && recommendationRows.size() > 0) {
            List<RecommendationDetails> recommendationDetails = new ArrayList<>();
            RecommendationDetails recommendationDetail;
            for (RecommendationRow recommendationRow : recommendationRows) {
                if (recommendationRow != null) {
                    recommendationDetail = new RecommendationDetails();
                    recommendationDetail.setRecommendationId(recommendationRow.getRecommendationId());
                    recommendationDetail.setRecommendationComment(recommendationRow.getComment());
                    recommendationDetails.add(recommendationDetail);
                }
            }
            interviewRecommendation.setRecommendations(recommendationDetails);
        }

        return  interviewRecommendation;
    }
}
