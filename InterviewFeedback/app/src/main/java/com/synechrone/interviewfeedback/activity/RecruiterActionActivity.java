package com.synechrone.interviewfeedback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.adapter.EmailAdapter;
import com.synechrone.interviewfeedback.adapter.SuggestionAdapter;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.ws.APIClient;
import com.synechrone.interviewfeedback.ws.APIService;
import com.synechrone.interviewfeedback.ws.request.EmailRequest;
import com.synechrone.interviewfeedback.ws.response.EmailId;
import com.synechrone.interviewfeedback.ws.response.Employee;

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

    private List<EmailId> selectedCandidateEmailIds = new ArrayList<>();
    private List<EmailId> selectedRecipientEmailIds = new ArrayList<>();
    private String recruiterEmailId;
    private SuggestionAdapter<EmailId> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_action);
        setToolbar(getString(R.string.activity_recruiter_title), false);
        initializeView();
        getCandidateEmails();
    }

    private void initializeView() {
        inputLayoutCandidateEmails = findViewById(R.id.inputLayoutCandidateEmails);
        autoTextEmail = findViewById(R.id.autoTextEmail);
        inputLayoutRecipientEmails = findViewById(R.id.inputLayoutRecipientEmails);
        recipientEmail = findViewById(R.id.editTextRecipientEmail);

        recipientEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String email = recipientEmail.getText().toString();
                    EmailId emailId = new EmailId();
                    emailId.setEmailId(email);
                    selectedRecipientEmailIds.add(emailId);
                    recipientEmail.setText("");
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
        EmailAdapter adapter = new EmailAdapter(this, selectedCandidateEmailIds);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerViewRecipient = findViewById(R.id.recycler_view_recipient_email);
        EmailAdapter adapterRecipient = new EmailAdapter(this, selectedRecipientEmailIds);
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
                recruiterEmailId = employee.getEmailId();
            }
        }
    }

    private void handleSendEmail() {
        boolean isValidRequest = validateRequest();
        if (isValidRequest) {
            EmailRequest emailIds = new EmailRequest();
            emailIds.setSenderEmailId(recruiterEmailId);
            emailIds.setCandidateEmailIds(selectedCandidateEmailIds);
            emailIds.setRecipientEmailIds(selectedRecipientEmailIds);

            APIService apiService = APIClient.getInstance();
            Call<Void> call = apiService.sendEmail(emailIds);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    handleSuccess();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    Log.e(AppConstants.TAG, throwable.toString());
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
        }

        if (selectedRecipientEmailIds.size() == 0) {
            String message = getString(R.string.error_candidate_name);
            inputLayoutRecipientEmails.setError(message);
            recipientEmail.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }
        return true;
    }

    private void handleSuccess() {
        Toast.makeText(this, "Report has been successfully sent.", Toast.LENGTH_LONG).show();
    }

    private void getCandidateEmails() {
        APIService apiService = APIClient.getInstance();
        Call<List<EmailId>> call = apiService.getCandidateEmailIds();
        call.enqueue(new Callback<List<EmailId>>() {
            @Override
            public void onResponse(Call<List<EmailId>> call, Response<List<EmailId>> response) {
                List<EmailId> emailIds = response.body();
                if (emailIds != null && emailIds.size() > 0) {
                    updateCandidateEmails(emailIds);
                }
            }

            @Override
            public void onFailure(Call<List<EmailId>> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
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
                }
            }
        });
    }

    public void addEmailToSuggestion(EmailId emailId) {
        adapter.add(emailId);
        adapter.notifyDataSetChanged();
    }
}
