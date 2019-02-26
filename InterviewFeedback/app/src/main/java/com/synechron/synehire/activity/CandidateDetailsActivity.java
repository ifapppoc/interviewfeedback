package com.synechron.synehire.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.synechron.synehire.R;
import com.synechron.synehire.adapter.SuggestionAdapter;
import com.synechron.synehire.constants.AppConstants;
import com.synechron.synehire.exception.NoConnectivityException;
import com.synechron.synehire.utility.PrefManager;
import com.synechron.synehire.ws.APIClient;
import com.synechron.synehire.ws.APIService;
import com.synechron.synehire.ws.request.InterviewPostRequest;
import com.synechron.synehire.ws.response.Candidate;
import com.synechron.synehire.ws.response.InterviewLevel;
import com.synechron.synehire.ws.response.InterviewMode;
import com.synechron.synehire.ws.response.Employee;
import com.synechron.synehire.ws.response.Technology;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidateDetailsActivity extends BaseActivity {

    private EditText candidateFirstName;
    private EditText candidateEmailId;
    private AutoCompleteTextView technology;
    private AutoCompleteTextView recruiterName;
    private AutoCompleteTextView level;
    private AutoCompleteTextView mode;
    private TextInputLayout inputCandidateFirstName;
    private TextInputLayout inputCandidateEmail;
    private TextInputLayout inputTechnology;
    private TextInputLayout inputRecruiter;
    private TextInputLayout inputLevel;
    private TextInputLayout inputMode;
    private ProgressBar progressBar;

    private int selectedTechnologyId = -1;
    private String selectedPanelId = null;
    private String selectedRecruiterId = null;
    private int selectedLevelId = -1;
    private int selectedModeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatesinfo);
        setToolbar(getString(R.string.activity_title_candidate_details), false);
        initializeView();
        getTechnologies();
        getRecruiters();
        getInterviewLevels();
        getInterviewModes();
    }

    private void initializeView() {
        progressBar = findViewById(R.id.progress_circular);
        inputCandidateFirstName = findViewById(R.id.inputLayoutCandidateFirstName);
        inputCandidateEmail = findViewById(R.id.inputLayoutCandidateEmail);
        inputTechnology = findViewById(R.id.inputLayoutTechnologyTested);
        inputRecruiter = findViewById(R.id.inputLayoutRecruiterName);
        inputLevel = findViewById(R.id.inputLayoutLevel);
        inputMode = findViewById(R.id.inputLayoutMode);
        candidateFirstName = findViewById(R.id.candidateFirstName);
        candidateFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String candidateName = candidateFirstName.getText().toString();
                if (!hasFocus && !(candidateName.matches(AppConstants.VALID_TEXT_PATTERN))) {
                    String message = getString(R.string.error_candidate_name);
                    inputCandidateFirstName.setError(message);
                    candidateFirstName.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputCandidateFirstName.setError(null);
                    candidateFirstName.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputCandidateFirstName.setErrorEnabled(false);
                }
            }
        });

        candidateEmailId = findViewById(R.id.candidateEmail);
        candidateEmailId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String candidateEmail = candidateEmailId.getText().toString();
                if (!hasFocus && !(candidateEmail.matches(AppConstants.VALID_EMAIL_PATTERN))) {
                    String message = getString(R.string.error_candidate_email);
                    inputCandidateEmail.setError(message);
                    candidateEmailId.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputCandidateEmail.setError(null);
                    candidateEmailId.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputCandidateEmail.setErrorEnabled(false);
                }
            }
        });

        technology = findViewById(R.id.technologyTested);
        technology.setInputType(InputType.TYPE_NULL);
        technology.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String technologyName = technology.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = technology.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(technologyName.compareTo(temp) == 0) {
                            return;
                        }
                    }

                    technology.setText("");
                    String message = getString(R.string.error_technology);
                    inputTechnology.setError(message);
                    technology.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputTechnology.setError(null);
                    technology.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputTechnology.setErrorEnabled(false);
                }
            }
        });
        technology.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                technology.showDropDown();
                return false;
            }
        });

        recruiterName = findViewById(R.id.recruiterName);
        recruiterName.setInputType(InputType.TYPE_NULL);
        recruiterName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String recruiter = recruiterName.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = recruiterName.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(recruiter.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    recruiterName.setText("");
                    String message = getString(R.string.error_recruiter_name);
                    inputRecruiter.setError(message);
                    recruiterName.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputRecruiter.setError(null);
                    recruiterName.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputRecruiter.setErrorEnabled(false);
                }
            }
        });
        recruiterName.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                recruiterName.showDropDown();
                return false;
            }
        });

        level = findViewById(R.id.interviewLevel);
        level.setInputType(InputType.TYPE_NULL);
        level.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String interviewLevel = level.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = level.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(interviewLevel.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    level.setText("");
                    String message = getString(R.string.error_interview_level);
                    inputLevel.setError(message);
                    level.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputLevel.setError(null);
                    level.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputLevel.setErrorEnabled(false);
                }
            }
        });
        level.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                level.showDropDown();
                return false;
            }
        });

        mode = findViewById(R.id.interviewMode);
        mode.setInputType(InputType.TYPE_NULL);
        mode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String interviewMode = mode.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = mode.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(interviewMode.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    mode.setText("");
                    String message = getString(R.string.error_interview_mode);
                    inputMode.setError(message);
                    mode.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputMode.setError(null);
                    mode.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputMode.setErrorEnabled(false);;
                }
            }
        });
        mode.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                mode.showDropDown();
                return false;
            }
        });

        TextView interviewTime = findViewById(R.id.interviewDate);
        interviewTime.setText(formatDate());

        Button submitButton = findViewById(R.id.submit_Button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCandidateDetails();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            Employee panelist = extras.getParcelable(AppConstants.KEY_EMPLOYEE);
            if (panelist != null) {
                selectedPanelId = panelist.getEmployeeId();
                setToolbarTitle("Welcome, "+ panelist.getFirstName() + " " + panelist.getLastName());
            }
        }
    }

    private String formatDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(new Date());
        return  formattedDate;
    }

    public void getTechnologies() {
        APIService apiService = APIClient.getInstance(CandidateDetailsActivity.this);
        Call<List<Technology>> call = apiService.getTechnologies();
        call.enqueue(new Callback<List<Technology>>() {
            @Override
            public void onResponse(Call<List<Technology>> call, Response<List<Technology>> response) {
                List<Technology> technologies = response.body();
                if (technologies != null && technologies.size() > 0) {
                    enableTechnologyAutoSuggestion(technologies);
                }
            }

            @Override
            public void onFailure(Call<List<Technology>> call, Throwable throwable) {
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    public void getRecruiters() {
        APIService apiService = APIClient.getInstance(CandidateDetailsActivity.this);
        Call<List<Employee>> call = apiService.getRecruiters();
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                List<Employee> recruiters = response.body();
                if (recruiters != null && recruiters.size() > 0) {
                    enableRecruiterAutoSuggestion(recruiters);
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable throwable) {
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    public void getInterviewLevels() {
        APIService apiService = APIClient.getInstance(CandidateDetailsActivity.this);
        Call<List<InterviewLevel>> call = apiService.getInterviewLevels();
        call.enqueue(new Callback<List<InterviewLevel>>() {
            @Override
            public void onResponse(Call<List<InterviewLevel>> call, Response<List<InterviewLevel>> response) {
                List<InterviewLevel> interviewLevels = response.body();
                if (interviewLevels != null && interviewLevels.size() > 0) {
                    enableInterviewLevelAutoSuggestion(interviewLevels);
                }
            }

            @Override
            public void onFailure(Call<List<InterviewLevel>> call, Throwable throwable) {
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    public void getInterviewModes() {
        APIService apiService = APIClient.getInstance(CandidateDetailsActivity.this);
        Call<List<InterviewMode>> call = apiService.getInterviewModes();
        call.enqueue(new Callback<List<InterviewMode>>() {
            @Override
            public void onResponse(Call<List<InterviewMode>> call, Response<List<InterviewMode>> response) {
                List<InterviewMode> interviewModes = response.body();
                if (interviewModes != null && interviewModes.size() > 0) {
                    enableInterviewModeAutoSuggestion(interviewModes);
                }
            }

            @Override
            public void onFailure(Call<List<InterviewMode>> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private void enableTechnologyAutoSuggestion(List<Technology> technologies) {
        SuggestionAdapter<Technology> adapter = new SuggestionAdapter<>(this, technologies);
        technology.setThreshold(0);
        technology.setAdapter(adapter);

        technology.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Technology selectedTechnology = parent != null ? (Technology) parent.getItemAtPosition(position) : null;
                selectedTechnologyId = selectedTechnology != null ? selectedTechnology.getTechId() : -1;
            }
        });
    }

    private void enableRecruiterAutoSuggestion(List<Employee> recruiters) {
        SuggestionAdapter<Employee> adapter = new SuggestionAdapter<>(this, recruiters);
        recruiterName.setThreshold(0);
        recruiterName.setAdapter(adapter);

        recruiterName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee selectedRecruiter = parent != null ? (Employee) parent.getItemAtPosition(position) : null;
                selectedRecruiterId = selectedRecruiter != null ? selectedRecruiter.getEmployeeId() : null;
            }
        });
    }

    private void enableInterviewLevelAutoSuggestion(List<InterviewLevel> interviewLevels) {
        SuggestionAdapter<InterviewLevel> adapter = new SuggestionAdapter<>(this, interviewLevels);
        level.setThreshold(0);
        level.setAdapter(adapter);

        level.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InterviewLevel selectedLevel = parent != null ? (InterviewLevel) parent.getItemAtPosition(position) : null;
                selectedLevelId = selectedLevel != null ? selectedLevel.getLevelId() : -1;
            }
        });
    }

    private void enableInterviewModeAutoSuggestion(List<InterviewMode> interviewModes) {
        SuggestionAdapter<InterviewMode> adapter = new SuggestionAdapter<>(this, interviewModes);
        mode.setThreshold(0);
        mode.setAdapter(adapter);

        mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InterviewMode selectedMode = parent != null ? (InterviewMode) parent.getItemAtPosition(position) : null;
                selectedModeId = selectedMode != null ? selectedMode.getId() : -1;
            }
        });
    }

    private void submitCandidateDetails() {
        boolean isValidDetails = validateCandidateDetails();
        if (isValidDetails) {
            InterviewPostRequest request = new InterviewPostRequest();
            request.setTechId(selectedTechnologyId);
            request.setPanelId(selectedPanelId);
            request.setRecruiterId(selectedRecruiterId);
            request.setLevelId(selectedLevelId);
            request.setInterviewModeId(selectedModeId);

            Candidate candidate = new Candidate();
            candidate.setCandidateEmailID(candidateEmailId.getText().toString());
            candidate.setFirstName(candidateFirstName.getText().toString());
            candidate.setLastName("");
            candidate.setTechId(selectedTechnologyId);
            candidate.setPrimaryContactNumber("");
            candidate.setAlternateContactNumber("");

            request.setCandidate(candidate);

            submitInterview(request);
        }
    }

    private boolean validateCandidateDetails() {
        String candidateName = candidateFirstName.getText().toString();
        if (!candidateName.isEmpty()) {
            inputCandidateFirstName.setError(null);
            inputCandidateFirstName.setErrorEnabled(false);
            candidateFirstName.setBackgroundResource(R.drawable.edit_text_bg_selector);
            candidateFirstName.clearFocus();
        } else {
            String message = getString(R.string.error_candidate_name);
            inputCandidateFirstName.setError(message);
            candidateFirstName.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        String candidateEmail = candidateEmailId.getText().toString();
        if (!candidateEmail.isEmpty()) {
            inputCandidateEmail.setError(null);
            inputCandidateEmail.setErrorEnabled(false);
            candidateEmailId.setBackgroundResource(R.drawable.edit_text_bg_selector);
            candidateEmailId.clearFocus();
        } else {
            String message = getString(R.string.error_candidate_email);
            inputCandidateEmail.setError(message);
            candidateEmailId.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        String technologyName = technology.getText().toString();
        if (!technologyName.isEmpty()) {
            inputTechnology.setError(null);
            inputTechnology.setErrorEnabled(false);
            technology.setBackgroundResource(R.drawable.edit_text_bg_selector);
            technology.clearFocus();
        } else {
            String message = getString(R.string.error_technology);
            inputTechnology.setError(message);
            technology.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        String recruiter = recruiterName.getText().toString();
        if (!recruiter.isEmpty()) {
            inputRecruiter.setError(null);
            inputRecruiter.setErrorEnabled(false);
            recruiterName.setBackgroundResource(R.drawable.edit_text_bg_selector);
            recruiterName.clearFocus();
        } else {
            String message = getString(R.string.error_recruiter_name);
            inputRecruiter.setError(message);
            recruiterName.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        String interviewLevel = level.getText().toString();
        if (!interviewLevel.isEmpty()) {
            inputLevel.setError(null);
            inputLevel.setErrorEnabled(false);
            level.setBackgroundResource(R.drawable.edit_text_bg_selector);
            level.clearFocus();
        } else {
            String message = getString(R.string.error_interview_level);
            inputLevel.setError(message);
            level.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        String interviewMode = mode.getText().toString();
        if (!interviewMode.isEmpty()) {
            inputMode.setError(null);
            inputMode.setErrorEnabled(false);
            mode.setBackgroundResource(R.drawable.edit_text_bg_selector);
            mode.clearFocus();
        } else {
            String message = getString(R.string.error_interview_mode);
            inputMode.setError(message);
            mode.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        return true;
    }

    private void submitInterview(final InterviewPostRequest request) {
        progressBar.setVisibility(View.VISIBLE);
        APIService apiService = APIClient.getInstance(CandidateDetailsActivity.this);
        Call<JsonObject> call = apiService.saveInterviewDetails(request);
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
                            Long interviewId = jsonObject.getLong(AppConstants.INTERVIEW_ID);
                            if (interviewId > 0) {
                                PrefManager.saveInterviewId(CandidateDetailsActivity.this, interviewId);
                                PrefManager.saveInterviewLevelId(CandidateDetailsActivity.this, selectedLevelId);
                                navigateToDiscussionDetails();
                            }
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

    private void navigateToDiscussionDetails() {
        if (selectedLevelId == 4) {
            Intent intent = new Intent(this, RecommendationActivity.class);
            intent.putExtra(AppConstants.KEY_LEVEL_4, true);
            startActivity(intent);
            CandidateDetailsActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
        } else {
            Intent intent = new Intent(this, DiscussionDetailsActivity.class);
            intent.putExtra(AppConstants.KEY_TECHNOLOGY, selectedTechnologyId);
            startActivity(intent);
            CandidateDetailsActivity.this.finish();
            overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
        }
    }
}
