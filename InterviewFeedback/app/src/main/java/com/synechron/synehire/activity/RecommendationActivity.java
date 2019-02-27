package com.synechron.synehire.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.synechron.synehire.R;
import com.synechron.synehire.adapter.InterviewSummaryAdaptor;
import com.synechron.synehire.adapter.RecommendationAdapter;
import com.synechron.synehire.constants.AppConstants;
import com.synechron.synehire.domain.RecommendationRow;
import com.synechron.synehire.exception.NoConnectivityException;
import com.synechron.synehire.utility.PrefManager;
import com.synechron.synehire.ws.APIClient;
import com.synechron.synehire.ws.APIService;
import com.synechron.synehire.ws.request.InterviewRecommendation;
import com.synechron.synehire.ws.request.RecommendationDetails;
import com.synechron.synehire.ws.response.Employee;
import com.synechron.synehire.ws.response.InterviewSummary;
import com.synechron.synehire.ws.response.Recommendation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationActivity extends BaseActivity {
    private RelativeLayout rlParent;
    private RecyclerView recyclerViewRecommendation;
    private ProgressBar progressBar;
    private Button buttonSubmitAssessment;

    private RecommendationAdapter adapter;
    private List<InterviewSummary> interviewSummaries;
    private long interviewId;
    private boolean isSubmitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        Intent intent = getIntent();
        boolean isStakeholder = false;
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            isStakeholder = extras.getBoolean(AppConstants.KEY_LEVEL_4);
        }
        setToolbar(getString(R.string.activity_recommendation_title), !isStakeholder);
        initializeView();
        int levelId = PrefManager.getInterviewLevelId(RecommendationActivity.this);
        getRecommendations(levelId);
        interviewId = PrefManager.getInterviewId(RecommendationActivity.this);
        if (!isStakeholder) {
            getInterviewSummary();
        }
    }

    private void initializeView() {
        rlParent = findViewById(R.id.rlParent);
        progressBar = findViewById(R.id.progress_circular);
        recyclerViewRecommendation = findViewById(R.id.recycler_view_recommendation);
        buttonSubmitAssessment = findViewById(R.id.submit_assessment);
        buttonSubmitAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSubmitted) {
                    handleSubmitAssessment();
                } else {
                    getEmployeeRole();
                }
            }
        });
    }

    private void handleSubmitAssessment() {
        List<RecommendationRow> recommendationRows = adapter.getRecommendations();
        if (recommendationRows != null && recommendationRows.size() > 0) {
            boolean isValid = validateComment(recommendationRows);
            if (isValid) {
                submitInterviewRecommendations(interviewId, recommendationRows);
            } else {
                Toast.makeText(RecommendationActivity.this, getString(R.string.error_enter_recommendation_comments), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateComment(List<RecommendationRow> recommendationRows) {
        for (RecommendationRow recommendationRow : recommendationRows) {
            if (recommendationRow.getComment() == null || recommendationRow.getComment().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void getRecommendations(int levelId) {
        progressBar.setVisibility(View.VISIBLE);
        APIService apiService = APIClient.getInstance(RecommendationActivity.this);
        Call<List<Recommendation>> call = apiService.getRecommendations(levelId);
        call.enqueue(new Callback<List<Recommendation>>() {
            @Override
            public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
                progressBar.setVisibility(View.GONE);
                List<Recommendation> recommendations = response.body();
                if (recommendations != null && recommendations.size() > 0) {
                    updateRecommendation(recommendations);
                } else {
                    showError("");
                }
            }

            @Override
            public void onFailure(Call<List<Recommendation>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
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
        APIService apiService = APIClient.getInstance(RecommendationActivity.this);
        Call<List<InterviewSummary>> call = apiService.getInterviewSummaries(interviewId);
        call.enqueue(new Callback<List<InterviewSummary>>() {
            @Override
            public void onResponse(Call<List<InterviewSummary>> call, Response<List<InterviewSummary>> response) {
                List<InterviewSummary> interviewSummaryList = response.body();
                if (interviewSummaryList != null && interviewSummaryList.size() > 0) {
                    interviewSummaries = interviewSummaryList;
                }
            }

            @Override
            public void onFailure(Call<List<InterviewSummary>> call, Throwable throwable) {
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    private void submitInterviewSummary(long interviewId) {
        APIService apiService = APIClient.getInstance(RecommendationActivity.this);
        Call<JsonObject> call = apiService.saveInterviewSummary(interviewId);
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
                            handleSuccess();
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
                showError("");
            }
        });
    }

    private void handleSuccess() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
        isSubmitted = true;
        buttonSubmitAssessment.setText(R.string.button_setup_another_interview);
        showSuccess(getString(R.string.interview_feedback_success_msg));
    }

    private void submitInterviewRecommendations(long interviewId, List<RecommendationRow> recommendationRows) {
        progressBar.setVisibility(View.VISIBLE);
        APIService apiService = APIClient.getInstance(RecommendationActivity.this);
        InterviewRecommendation interviewRecommendation = new InterviewRecommendation();
        interviewRecommendation.setInterviewId(interviewId);
        enrichWithInterviewRecommendation(interviewRecommendation, recommendationRows);
        Call<JsonObject> call = apiService.saveInterviewRecommendations(interviewRecommendation);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    try {
                        JsonObject json = response.body();
                        JSONObject jsonObject = new JSONObject(json.toString());
                        String status = jsonObject.getString(AppConstants.STATUS);
                        if (AppConstants.SUCCESS.equalsIgnoreCase(status)) {
                            submitInterview();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            String message = jsonObject.getString(AppConstants.ERROR_MESSAGE);
                            showError(message);
                        }
                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        showError("");
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
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

    private void submitInterview() {
        submitInterviewSummary(interviewId);
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

    private void showSummary() {
        LayoutInflater inflater = (LayoutInflater) RecommendationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_summary_layout,null);
        RelativeLayout rlParentBg = layout.findViewById(R.id.rlParentDialog);
        rlParentBg.setBackground(ActivityCompat.getDrawable(RecommendationActivity.this, R.drawable.summary_dialog_white_bg));
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        InterviewSummaryAdaptor tAdapter = new InterviewSummaryAdaptor(this, interviewSummaries, null, true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(tAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        int dialogWindowWidth = (int) (displayWidth * 0.90f);
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

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    private void getEmployeeRole() {
        progressBar.setVisibility(View.VISIBLE);
        String emailId = PrefManager.getUserId(RecommendationActivity.this);
        APIService apiService = APIClient.getInstance(RecommendationActivity.this);
        Call<Employee> call = apiService.getEmployee(emailId);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                progressBar.setVisibility(View.GONE);
                Employee employee = response.body();
                if (employee != null) {
                    navigateToNextScreen(employee);
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    private void navigateToNextScreen(Employee employee) {
        PrefManager.clearInterviewId(RecommendationActivity.this);
        PrefManager.clearInterviewLevelId(RecommendationActivity.this);
        Intent intent = new Intent(this, CandidateDetailsActivity.class);
        intent.putExtra(AppConstants.KEY_EMPLOYEE, employee);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        RecommendationActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }
}
