package com.synechrone.interviewfeedback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.adapter.SuggestionAdapter;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.utility.PrefManager;
import com.synechrone.interviewfeedback.ws.APIClient;
import com.synechrone.interviewfeedback.ws.APIService;
import com.synechrone.interviewfeedback.ws.request.InterviewPostRequest;
import com.synechrone.interviewfeedback.ws.response.Candidate;
import com.synechrone.interviewfeedback.ws.response.InterviewLevel;
import com.synechrone.interviewfeedback.ws.response.InterviewMode;
import com.synechrone.interviewfeedback.ws.response.Employee;
import com.synechrone.interviewfeedback.ws.response.Technology;

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
    private AutoCompleteTextView panelistName;
    private AutoCompleteTextView recruiterName;
    private AutoCompleteTextView level;
    private AutoCompleteTextView mode;
    private TextInputLayout inputCandidateFirstName;
    private TextInputLayout inputCandidateEmail;
    private TextInputLayout inputTechnology;
    private TextInputLayout inputPanelName;
    private TextInputLayout inputRecruiter;
    private TextInputLayout inputLevel;
    private TextInputLayout inputMode;

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
        inputCandidateFirstName = findViewById(R.id.inputLayoutCandidateFirstName);
        inputCandidateEmail = findViewById(R.id.inputLayoutCandidateEmail);
        inputTechnology = findViewById(R.id.inputLayoutTechnologyTested);
        inputPanelName = findViewById(R.id.inputLayoutPanelName);
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

        panelistName = findViewById(R.id.interviewerName);
        panelistName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String panel = panelistName.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = panelistName.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(panel.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    panelistName.setText("");
                    String message = getString(R.string.error_panel_name);
                    inputPanelName.setError(message);
                    panelistName.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputPanelName.setError(null);
                    panelistName.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputPanelName.setErrorEnabled(false);
                }
            }
        });
        panelistName.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                panelistName.showDropDown();
                return false;
            }
        });

        recruiterName = findViewById(R.id.recruiterName);
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
        interviewTime.setText("Interview Date: " + formatDate());

        Button submitButton = findViewById(R.id.submit_Button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCandidateDetails();
            }
        });
    }

    private String formatDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(new Date());
        return  formattedDate;
    }

    public void getTechnologies() {
        APIService apiService = APIClient.getInstance();
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
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    public void getPanelists(int selectedTechnologyId) {
        APIService apiService = APIClient.getInstance();
        Call<List<Employee>> call = apiService.getPanelist(selectedTechnologyId);
        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                List<Employee> interviewPanelists = response.body();
                if (interviewPanelists != null && interviewPanelists.size() > 0) {
                    enablePanelistAutoSuggestion(interviewPanelists);
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    public void getRecruiters() {
        APIService apiService = APIClient.getInstance();
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
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    public void getInterviewLevels() {
        APIService apiService = APIClient.getInstance();
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
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    public void getInterviewModes() {
        APIService apiService = APIClient.getInstance();
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
                getPanelists(selectedTechnologyId);
            }
        });
    }

    private void enablePanelistAutoSuggestion(List<Employee> interviewPanelists) {
        SuggestionAdapter<Employee> adapter = new SuggestionAdapter<>(this, interviewPanelists);
        panelistName.setThreshold(0);
        panelistName.setAdapter(adapter);

        panelistName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee selectedPanelist = parent != null ? (Employee) parent.getItemAtPosition(position) : null;
                selectedPanelId = selectedPanelist != null ? selectedPanelist.getEmployeeId() : null;
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

    private void submitInterview(InterviewPostRequest request) {
        APIService apiService = APIClient.getInstance();
        Call<Long> call = apiService.saveInterviewDetails(request);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Long interviewId = response.body();
                if (interviewId != null && interviewId > 0) {
                    PrefManager.saveInterviewLevelId(CandidateDetailsActivity.this, selectedLevelId);
                    PrefManager.saveInterviewId(CandidateDetailsActivity.this, interviewId);
                    navigateToTopicScreen();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private void navigateToTopicScreen() {
        Intent intent = new Intent(this, DiscussionDetailsActivity.class);
        intent.putExtra(AppConstants.KEY_TECHNOLOGY, selectedTechnologyId);
        startActivity(intent);
        CandidateDetailsActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }
}
