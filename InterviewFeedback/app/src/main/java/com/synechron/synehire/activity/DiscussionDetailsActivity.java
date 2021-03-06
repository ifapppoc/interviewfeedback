package com.synechron.synehire.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.synechron.synehire.R;
import com.synechron.synehire.adapter.InterviewSummaryAdaptor;
import com.synechron.synehire.adapter.OutcomeAdapter;
import com.synechron.synehire.adapter.SuggestionAdapter;
import com.synechron.synehire.constants.AppConstants;
import com.synechron.synehire.exception.NoConnectivityException;
import com.synechron.synehire.listener.DoneOnEditorActionListener;
import com.synechron.synehire.utility.PrefManager;
import com.synechron.synehire.ws.APIClient;
import com.synechron.synehire.ws.APIService;
import com.synechron.synehire.ws.request.DiscussionDetails;
import com.synechron.synehire.ws.response.DiscussionMode;
import com.synechron.synehire.ws.response.DiscussionOutcome;
import com.synechron.synehire.ws.response.InterviewSummary;
import com.synechron.synehire.ws.response.SubTopic;
import com.synechron.synehire.ws.response.Topic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionDetailsActivity extends BaseActivity {
    private RelativeLayout rlParent;
    private LinearLayout llTopicAndSubtopicSection;
    private LinearLayout llDiscussionDetailsSection;
    private TextView textViewHeading;
    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private TextInputLayout inputLayoutOutcome;
    private AutoCompleteTextView autoTextOutcome;
    private TextInputLayout inputLayoutMainTopic;
    private AutoCompleteTextView autoMainTopic;
    private TextInputLayout inputLayoutSubTopic;
    private AutoCompleteTextView autoSubTopic;
    private EditText editTextComment;
    private ProgressBar progressBar;

    private int selectedSubTopicId = -1;
    private ArrayList<InterviewSummary> interviewSummaries = new ArrayList<>();
    private List<DiscussionOutcome> selectedOutcomes = new ArrayList<>();
    private List<DiscussionOutcome> suggestionOutcomes = new ArrayList<>();
    private List<DiscussionOutcome> outcomes;
    private SuggestionAdapter<DiscussionOutcome> adapter;
    private OutcomeAdapter outcomeAdapter;
    private boolean isDiscussionSubmitted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_outcome);
        setToolbar(getString(R.string.activity_title_discussion), false);
        initializeView();
        getDiscussionModes();
        getDiscussionsOutcome();
    }

    private void initializeView() {
        rlParent = findViewById(R.id.rlParent);
        progressBar = findViewById(R.id.progress_circular);
        llTopicAndSubtopicSection = findViewById(R.id.llTopicAndSubtopicSection);
        llDiscussionDetailsSection = findViewById(R.id.llDiscussionDetailsSection);
        inputLayoutMainTopic = findViewById(R.id.inputLayoutMainTopic);
        autoMainTopic = findViewById(R.id.autoTextMainTopic);
        autoMainTopic.setInputType(InputType.TYPE_NULL);
        autoMainTopic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String mainTopic = autoMainTopic.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = autoMainTopic.getAdapter();
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if (mainTopic.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    autoMainTopic.setText("");
                    String message = getString(R.string.error_enter_ta);
                    inputLayoutMainTopic.setError(message);
                    autoMainTopic.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputLayoutMainTopic.setError(null);
                    autoMainTopic.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputLayoutMainTopic.setErrorEnabled(false);
                }
            }
        });

        autoMainTopic.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autoMainTopic.showDropDown();
                return false;
            }
        });
        inputLayoutSubTopic = findViewById(R.id.inputLayoutSubTopic);
        autoSubTopic = findViewById(R.id.autoTextSubTopic);
        autoSubTopic.setInputType(InputType.TYPE_NULL);
        autoSubTopic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String subTopic = autoSubTopic.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = autoSubTopic.getAdapter();
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(subTopic.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    autoSubTopic.setText("");
                    String message = getString(R.string.error_enter_ct);
                    inputLayoutSubTopic.setError(message);
                    autoSubTopic.setBackgroundResource(R.drawable.edit_text_bg_error);
                } else {
                    inputLayoutSubTopic.setError(null);
                    autoSubTopic.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputLayoutSubTopic.setErrorEnabled(false);
                }
            }
        });
        autoSubTopic.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autoSubTopic.showDropDown();
                return false;
            }
        });

        textViewHeading = findViewById(R.id.textView_heading);
        checkbox1 = findViewById(R.id.checkbox_1);
        checkbox2 = findViewById(R.id.checkbox_2);
        checkbox3 = findViewById(R.id.checkbox_3);
        inputLayoutOutcome = findViewById(R.id.inputLayoutOutcome);
        autoTextOutcome = findViewById(R.id.autoTextOutcome);
        autoTextOutcome.setInputType(InputType.TYPE_NULL);
        autoTextOutcome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String outcome = autoTextOutcome.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = autoTextOutcome.getAdapter();
                    for(int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if(outcome.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    autoTextOutcome.setText("");
                }
            }
        });
        autoTextOutcome.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autoTextOutcome.showDropDown();
                return false;
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        outcomeAdapter = new OutcomeAdapter(this, selectedOutcomes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(outcomeAdapter);
        editTextComment = findViewById(R.id.editTextComment);
        editTextComment.setOnEditorActionListener(new DoneOnEditorActionListener());
        Button buttonContinue = findViewById(R.id.button_continue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInterviewSummary(1);
            }
        });

        Button buttonFinish = findViewById(R.id.button_finish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDiscussionSubmitted) {
                    navigateToInterviewSummary();
                } else {
                    saveInterviewSummary(2);
                }
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            int techId = extras.getInt(AppConstants.KEY_TECHNOLOGY);
            getTopics(techId);
        }
    }

    private void showDiscussionDetailSection() {
        String mainTopic = autoMainTopic.getText().toString();
        String subTopic = autoSubTopic.getText().toString();
        textViewHeading.setText(mainTopic + ": " + subTopic);
        llDiscussionDetailsSection.setVisibility(View.VISIBLE);
        llTopicAndSubtopicSection.setVisibility(View.GONE);
    }

    private void hideDiscussionDetailSection() {
        checkbox1.setChecked(false);
        checkbox2.setChecked(false);
        checkbox3.setChecked(false);
        autoMainTopic.setText("");
        autoSubTopic.setText("");
        autoTextOutcome.setText("");
        editTextComment.setText("");
        selectedOutcomes.clear();
        suggestionOutcomes.clear();
        suggestionOutcomes.addAll(outcomes);
        adapter.clear();
        adapter.addAll(outcomes);
        adapter.notifyDataSetChanged();
        outcomeAdapter.notifyDataSetChanged();
        llDiscussionDetailsSection.setVisibility(View.GONE);
        llTopicAndSubtopicSection.setVisibility(View.VISIBLE);
    }

    private void getTopics(int techId) {
        APIService apiService = APIClient.getInstance(DiscussionDetailsActivity.this);
        Call<List<Topic>> call = apiService.getTopics(techId);
        call.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                List<Topic> topics = response.body();
                if (topics != null && topics.size() > 0) {
                    updateUIForTopics(topics);
                } else {
                    showError("");
                }
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable throwable) {
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    public void getSubTopics(int selectedTopicId) {
        APIService apiService = APIClient.getInstance(DiscussionDetailsActivity.this);
        Call<List<SubTopic>> call = apiService.getSubTopics(selectedTopicId);
        call.enqueue(new Callback<List<SubTopic>>() {
            @Override
            public void onResponse(Call<List<SubTopic>> call, Response<List<SubTopic>> response) {
                List<SubTopic> subTopics = response.body();
                if (subTopics != null && subTopics.size() > 0) {
                    updateUIForSubTopics(subTopics);
                } else {
                    showError("");
                }
            }

            @Override
            public void onFailure(Call<List<SubTopic>> call, Throwable throwable) {
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    private void updateUIForTopics(List<Topic> topics) {
        SuggestionAdapter<Topic> adapter = new SuggestionAdapter<>(this, topics);
        autoMainTopic.setThreshold(0);//will start working from first character
        autoMainTopic.setAdapter(adapter);
        autoMainTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic selectedTopic = parent != null ? (Topic) parent.getItemAtPosition(position) : null;
                int selectedTopicId = selectedTopic != null ? selectedTopic.getTopicId() : -1;
                getSubTopics(selectedTopicId);
            }
        });
    }

    private void updateUIForSubTopics(List<SubTopic> subTopics) {
        SuggestionAdapter<SubTopic> adapter = new SuggestionAdapter<>(this, subTopics);
        autoSubTopic.setThreshold(0);//will start working from first character
        autoSubTopic.setAdapter(adapter);
        autoSubTopic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubTopic selectedSubTopic = parent != null ? (SubTopic) parent.getItemAtPosition(position) : null;
                selectedSubTopicId = selectedSubTopic != null ? selectedSubTopic.getSubtopicId() : -1;
                if (selectedSubTopicId > 0) {
                    showDiscussionDetailSection();
                }
            }
        });
    }

    public void getDiscussionModes() {
        APIService apiService = APIClient.getInstance(DiscussionDetailsActivity.this);
        Call<List<DiscussionMode>> call = apiService.getDiscussionsModes();
        call.enqueue(new Callback<List<DiscussionMode>>() {
            @Override
            public void onResponse(Call<List<DiscussionMode>> call, Response<List<DiscussionMode>> response) {
                List<DiscussionMode> discussionModes = response.body();
                if (discussionModes != null && discussionModes.size() == 3) {
                    DiscussionMode discussionMode1 = discussionModes.get(0);
                    if (discussionMode1 != null) {
                        checkbox1.setTag(discussionMode1);
                        checkbox1.setText(discussionMode1.getModeName());
                    }

                    DiscussionMode discussionMode2 = discussionModes.get(1);
                    if (discussionMode2 != null) {
                        checkbox2.setTag(discussionMode2);
                        checkbox2.setText(discussionMode2.getModeName());
                    }

                    DiscussionMode discussionMode3 = discussionModes.get(2);
                    if (discussionMode3 != null) {
                        checkbox3.setTag(discussionMode3);
                        checkbox3.setText(discussionMode3.getModeName());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<DiscussionMode>> call, Throwable throwable) {
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    public void getDiscussionsOutcome() {
        APIService apiService = APIClient.getInstance(DiscussionDetailsActivity.this);
        Call<List<DiscussionOutcome>> call = apiService.getDiscussionsOutcome();
        call.enqueue(new Callback<List<DiscussionOutcome>>() {
            @Override
            public void onResponse(Call<List<DiscussionOutcome>> call, Response<List<DiscussionOutcome>> response) {
                List<DiscussionOutcome> discussionOutcomes = response.body();
                if (discussionOutcomes != null && discussionOutcomes.size() > 0) {
                    updateCommentsAndOutcomes(discussionOutcomes);
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

    private void updateCommentsAndOutcomes(List<DiscussionOutcome> outcomes) {
        this.outcomes = outcomes;
        suggestionOutcomes = new ArrayList<>(outcomes);
        adapter = new SuggestionAdapter<>(this, suggestionOutcomes);
        autoTextOutcome.setThreshold(0);//will start working from first character
        autoTextOutcome.setAdapter(adapter);
        autoTextOutcome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DiscussionOutcome discussionOutcome = parent != null ? (DiscussionOutcome) parent.getItemAtPosition(position) : null;
                if (discussionOutcome != null) {
                    adapter.remove(discussionOutcome);
                    adapter.notifyDataSetChanged();
                    selectedOutcomes.add(discussionOutcome);
                    autoTextOutcome.setText("");
                    outcomeAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void addOutcomeToSuggestion(DiscussionOutcome discussionOutcome) {
        adapter.add(discussionOutcome);
        adapter.notifyDataSetChanged();
    }

    private void saveInterviewSummary(int requestCode) {
        boolean isValid = validateInterviewSummary();
        if (isValid) {
            DiscussionDetails discussionDetails = prepareDiscussionDetails();
            InterviewSummary interviewSummary = prepareInterviewSummary();
            interviewSummaries.add(interviewSummary);
            submitDiscussionDetails(discussionDetails, requestCode);
        }
    }

    private InterviewSummary prepareInterviewSummary() {
        InterviewSummary interviewSummary = new InterviewSummary();
        interviewSummary.setTopic(autoMainTopic.getText().toString());
        interviewSummary.setSubTopic(autoSubTopic.getText().toString());
        List<DiscussionMode> discussionModes = new ArrayList<>();
        if (checkbox1.isChecked()) {
            discussionModes.add((DiscussionMode) checkbox1.getTag());
        }

        if (checkbox2.isChecked()) {
            discussionModes.add((DiscussionMode) checkbox2.getTag());
        }

        if (checkbox3.isChecked()) {
            discussionModes.add((DiscussionMode) checkbox3.getTag());
        }
        interviewSummary.setDiscussionModes(discussionModes);
        List<DiscussionOutcome> discussionOutcomes = new ArrayList<>(selectedOutcomes);
        interviewSummary.setDiscussionOutcomes(discussionOutcomes);
        String comment = editTextComment.getText() != null ? editTextComment.getText().toString() : "";
        interviewSummary.setComment(comment);
        return interviewSummary;
    }

    private void navigateToInterviewSummary() {
        Intent intent = new Intent(this, InterviewSummaryActivity.class);
        startActivity(intent);
        DiscussionDetailsActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }

    private void submitDiscussionDetails(DiscussionDetails request, final int requestCode) {
        progressBar.setVisibility(View.VISIBLE);
        APIService apiService = APIClient.getInstance(DiscussionDetailsActivity.this);
        Call<JsonObject> call = apiService.saveDiscussionDetails(request);
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
                            handleNavigation(requestCode);
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

    private void handleNavigation(int requestCode) {
        isDiscussionSubmitted = true;
        switch (requestCode) {
            case 1:
                if (interviewSummaries.size() == 1) {
                    invalidateOptionsMenu();
                }
                hideDiscussionDetailSection();
                break;
            case 2:
                navigateToInterviewSummary();
                break;
        }
    }

    private boolean validateInterviewSummary() {
        String mainTopic = autoMainTopic.getText().toString();
        if (!mainTopic.isEmpty()) {
            inputLayoutMainTopic.setError(null);
            inputLayoutMainTopic.setErrorEnabled(false);
            autoMainTopic.setBackgroundResource(R.drawable.edit_text_bg_selector);
            autoMainTopic.clearFocus();
        } else {
            String message = getString(R.string.error_enter_ta);
            inputLayoutMainTopic.setError(message);
            autoMainTopic.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        String subTopic = autoSubTopic.getText().toString();
        if (!subTopic.isEmpty()) {
            inputLayoutSubTopic.setError(null);
            inputLayoutSubTopic.setErrorEnabled(false);
            autoSubTopic.setBackgroundResource(R.drawable.edit_text_bg_selector);
            autoSubTopic.clearFocus();
        } else {
            String message = getString(R.string.error_enter_ct);
            inputLayoutSubTopic.setError(message);
            autoSubTopic.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        if (!(checkbox1.isChecked() || checkbox2.isChecked() || checkbox3.isChecked())) {
            Toast.makeText(this, getString(R.string.error_select_mode_of_discussion), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedOutcomes != null && selectedOutcomes.size() > 0) {
            inputLayoutOutcome.setError(null);
            inputLayoutOutcome.setErrorEnabled(false);
            autoTextOutcome.setBackgroundResource(R.drawable.edit_text_bg_selector);
            autoTextOutcome.clearFocus();
        } else {
            String message = getString(R.string.error_enter_comments);
            inputLayoutOutcome.setError(message);
            autoTextOutcome.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        return true;
    }

    private DiscussionDetails prepareDiscussionDetails() {
        DiscussionDetails discussionDetails = new DiscussionDetails();
        discussionDetails.setSubTopicId(selectedSubTopicId);
        long interviewId = PrefManager.getInterviewId(DiscussionDetailsActivity.this);
        discussionDetails.setInterviewId(interviewId);
        List<Integer> discussionModeIds = new ArrayList<>();
        if (checkbox1.isChecked()) {
            DiscussionMode discussionMode = (DiscussionMode) checkbox1.getTag();
            discussionModeIds.add(discussionMode.getId());
        }

        if (checkbox2.isChecked()) {
            DiscussionMode discussionMode = (DiscussionMode) checkbox2.getTag();
            discussionModeIds.add(discussionMode.getId());
        }

        if (checkbox3.isChecked()) {
            DiscussionMode discussionMode = (DiscussionMode) checkbox3.getTag();
            discussionModeIds.add(discussionMode.getId());
        }

        discussionDetails.setDiscussionModeIds(discussionModeIds);

        List<Integer> discussionOutcomeIds = new ArrayList<>();
        for (DiscussionOutcome discussionOutcome : selectedOutcomes) {
            discussionOutcomeIds.add(discussionOutcome.getId());
        }
        discussionDetails.setOutcomesIds(discussionOutcomeIds);
        String comment = editTextComment.getText() != null ? editTextComment.getText().toString() : "";
        discussionDetails.setComment(comment);
        return discussionDetails;
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
        LayoutInflater inflater = (LayoutInflater) DiscussionDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_summary_layout,null);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        InterviewSummaryAdaptor tAdapter = new InterviewSummaryAdaptor(this, interviewSummaries, null, false);
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
}
