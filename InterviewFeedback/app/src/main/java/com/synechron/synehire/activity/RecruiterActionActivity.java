package com.synechron.synehire.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.synechron.synehire.adapter.EmailAdapter;
import com.synechron.synehire.adapter.SuggestionAdapter;
import com.synechron.synehire.constants.AppConstants;
import com.synechron.synehire.exception.NoConnectivityException;
import com.synechron.synehire.ws.APIClient;
import com.synechron.synehire.ws.APIService;
import com.synechron.synehire.ws.request.EmailRequest;
import com.synechron.synehire.ws.response.EmailId;
import com.synechron.synehire.ws.response.Employee;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecruiterActionActivity extends BaseActivity {
    private TextInputLayout inputLayoutCandidateEmails;
    private AutoCompleteTextView autoTextEmail;
    private TextInputLayout inputLayoutRecipientEmails;
    private EditText recipientEmail;
    private ProgressBar progressBar;

    private List<EmailId> selectedCandidateEmailIds = new ArrayList<>();
    private List<EmailId> selectedRecipientEmailIds = new ArrayList<>();
    private SuggestionAdapter<EmailId> adapter;
    private EmailAdapter emailAdapter;
    private EmailAdapter adapterRecipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_action);
        setToolbar(getString(R.string.activity_recruiter_title), false);
        initializeView();
        getCandidateEmails();
    }

    private void initializeView() {
        progressBar = findViewById(R.id.progress_circular);
        inputLayoutCandidateEmails = findViewById(R.id.inputLayoutCandidateEmails);
        autoTextEmail = findViewById(R.id.autoTextEmail);
        inputLayoutRecipientEmails = findViewById(R.id.inputLayoutRecipientEmails);
        recipientEmail = findViewById(R.id.editTextRecipientEmail);

        recipientEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String email = recipientEmail.getText().toString();
                    EmailId emailId = new EmailId();
                    emailId.setEmailId(email);
                    selectedRecipientEmailIds.add(emailId);
                    recipientEmail.setText("");
                    adapterRecipient.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        autoTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String outcome = autoTextEmail.getText().toString();
                if (!hasFocus) {
                    ListAdapter listAdapter = autoTextEmail.getAdapter();
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        String temp = listAdapter.getItem(i).toString();
                        if (outcome.compareTo(temp) == 0) {
                            return;
                        }
                    }
                    autoTextEmail.setText("");
                }
            }
        });
        autoTextEmail.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autoTextEmail.showDropDown();
                return false;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        emailAdapter = new EmailAdapter(this, selectedCandidateEmailIds);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(emailAdapter);

        RecyclerView recyclerViewRecipient = findViewById(R.id.recycler_view_recipient_email);
        adapterRecipient = new EmailAdapter(this, selectedRecipientEmailIds);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewRecipient.setLayoutManager(linearLayoutManager);
        recyclerViewRecipient.setAdapter(adapterRecipient);

        Button buttonSendEmail = findViewById(R.id.buttonSendEmail);
        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendEmail();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            Employee employee = extras.getParcelable(AppConstants.KEY_EMPLOYEE);
            if (employee != null) {
                setToolbarTitle("Welcome, " + employee.getFirstName() + " " + employee.getLastName());
            }
        }
    }

    private void handleSendEmail() {
        boolean isValidRequest = validateRequest();
        if (isValidRequest) {
            progressBar.setVisibility(View.VISIBLE);
            EmailRequest emailIds = new EmailRequest();
            emailIds.setSenderEmailId(AppConstants.SYNEHIRE_ID);
            emailIds.setCandidateEmailIds(selectedCandidateEmailIds);
            emailIds.setRecipientEmailIds(selectedRecipientEmailIds);

            APIService apiService = APIClient.getInstance(RecruiterActionActivity.this);
            Call<JsonObject> call = apiService.sendEmail(emailIds);
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
                                showSuccess(getString(R.string.interview_report_success_msg));
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

    private boolean validateRequest() {
        if (selectedCandidateEmailIds.size() == 0) {
            String message = getString(R.string.error_candidate_name);
            inputLayoutCandidateEmails.setError(message);
            autoTextEmail.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        } else {
            inputLayoutCandidateEmails.setError(null);
            autoTextEmail.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputLayoutCandidateEmails.setErrorEnabled(false);
        }

        if (selectedRecipientEmailIds.size() == 0) {
            String message = getString(R.string.error_recipient_email);
            inputLayoutRecipientEmails.setError(message);
            recipientEmail.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        } else {
            inputLayoutRecipientEmails.setError(null);
            recipientEmail.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputLayoutRecipientEmails.setErrorEnabled(false);
        }
        return true;
    }

    private void getCandidateEmails() {
        progressBar.setVisibility(View.VISIBLE);
        APIService apiService = APIClient.getInstance(RecruiterActionActivity.this);
        Call<List<EmailId>> call = apiService.getCandidateEmailIds();
        call.enqueue(new Callback<List<EmailId>>() {
            @Override
            public void onResponse(Call<List<EmailId>> call, Response<List<EmailId>> response) {
                progressBar.setVisibility(View.GONE);
                List<EmailId> emailIds = response.body();
                if (emailIds != null && emailIds.size() > 0) {
                    updateCandidateEmails(emailIds);
                }
            }

            @Override
            public void onFailure(Call<List<EmailId>> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                if (throwable instanceof NoConnectivityException) {
                    showError(throwable.getMessage());
                } else {
                    showError("");
                }
            }
        });
    }

    private void updateCandidateEmails(List<EmailId> emailIds) {
        List<EmailId> suggestionEmailIds = emailIds;
        adapter = new SuggestionAdapter<>(this, suggestionEmailIds);
        autoTextEmail.setThreshold(0);//will start working from first character
        autoTextEmail.setAdapter(adapter);
        autoTextEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmailId emailId = parent != null ? (EmailId) parent.getItemAtPosition(position) : null;
                if (emailId != null) {
                    adapter.remove(emailId);
                    adapter.notifyDataSetChanged();
                    selectedCandidateEmailIds.add(emailId);
                    autoTextEmail.setText("");
                    emailAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void addEmailToSuggestion(EmailId emailId) {
        adapter.add(emailId);
        adapter.notifyDataSetChanged();
    }
}
