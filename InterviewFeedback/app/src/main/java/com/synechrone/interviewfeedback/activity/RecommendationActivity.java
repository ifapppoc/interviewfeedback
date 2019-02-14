package com.synechrone.interviewfeedback.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.adapter.InterviewSummaryAdaptor;
import com.synechrone.interviewfeedback.adapter.RecommendationAdapter;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.RecommendationRow;
import com.synechrone.interviewfeedback.utility.PrefManager;
import com.synechrone.interviewfeedback.ws.APIClient;
import com.synechrone.interviewfeedback.ws.APIService;
import com.synechrone.interviewfeedback.ws.request.InterviewRecommendation;
import com.synechrone.interviewfeedback.ws.request.RecommendationDetails;
import com.synechrone.interviewfeedback.ws.response.InterviewSummary;
import com.synechrone.interviewfeedback.ws.response.Recommendation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationActivity extends BaseActivity {
    private RelativeLayout rlParent;
    private RecyclerView recyclerViewRecommendation;
    private RecommendationAdapter adapter;
    private List<InterviewSummary> interviewSummaries;
    private long interviewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        setToolbar(getString(R.string.activity_recommendation_title), true);
        initializeView();
        int levelId = PrefManager.getInterviewLevelId(RecommendationActivity.this);
        getRecommendations(levelId);
        interviewId = PrefManager.getInterviewId(RecommendationActivity.this);
        getInterviewSummary();
    }

    private void initializeView() {
        rlParent = findViewById(R.id.rlParent);
        recyclerViewRecommendation = findViewById(R.id.recycler_view_recommendation);
        Button buttonSubmitAssessment = findViewById(R.id.submit_assessment);
        buttonSubmitAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RecommendationRow> recommendationRows = adapter.getRecommendations();
                if (recommendationRows != null && recommendationRows.size() > 0) {
                    long interviewId = PrefManager.getInterviewId(RecommendationActivity.this);
                    submitInterviewSummary(interviewId);
                    submitInterviewRecommendations(interviewId, recommendationRows);
                }
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

    private void getInterviewSummary() {
        APIService apiService = APIClient.getInstance();
        Call<List<InterviewSummary>> call = apiService.getInterviewSummaries(interviewId);
        call.enqueue(new Callback<List<InterviewSummary>>() {
            @Override
            public void onResponse(Call<List<InterviewSummary>> call, Response<List<InterviewSummary>> response) {
                interviewSummaries = response.body();
            }

            @Override
            public void onFailure(Call<List<InterviewSummary>> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private void submitInterviewSummary(long interviewId) {
        APIService apiService = APIClient.getInstance();
        Call<Void> call = apiService.saveInterviewSummary(interviewId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e(AppConstants.TAG, "saved successfully");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private void submitInterviewRecommendations(long interviewId, List<RecommendationRow> recommendationRows) {
        APIService apiService = APIClient.getInstance();
        InterviewRecommendation interviewRecommendation = new InterviewRecommendation();
        interviewRecommendation.setInterviewId(interviewId);
        enrichWithInterviewRecommendation(interviewRecommendation, recommendationRows);
        Call<Void> call = apiService.saveInterviewRecommendations(interviewRecommendation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        MenuItem item = menu.findItem(R.id.action_summary);
        item.setVisible(interviewSummaries != null && interviewSummaries.size() > 0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_summary) {
            showSummary();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSummary(){
        LayoutInflater inflater = (LayoutInflater) RecommendationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_summary_layout,null);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        InterviewSummaryAdaptor tAdapter = new InterviewSummaryAdaptor(this, interviewSummaries, null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(tAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        int dialogWindowWidth = (int) (displayWidth * 0.80f);
        int dialogWindowHeight = (int) (displayHeight * 0.50f);

        final PopupWindow pw = new PopupWindow(layout, dialogWindowWidth, dialogWindowHeight, true);
        pw.setTouchable(true);
        pw.setOutsideTouchable(true);
        pw.setContentView(layout);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.showAtLocation(rlParent, Gravity.CENTER, 0, 0);
    }
}
