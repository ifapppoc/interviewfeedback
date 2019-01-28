package com.synechrone.interviewfeedback.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.CandidateDetails;
import com.synechrone.interviewfeedback.domain.TechnologyScope;
import com.synechrone.interviewfeedback.ws.response.Technology;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CandidateDetailsActivity extends BaseActivity {

    private EditText panelistName;
    private EditText candidatesName;
    private EditText candidateEmailId;
    private TextView interviewTime;
    private AutoCompleteTextView technology;
    private Button submitButton;
    private TextInputLayout inputPanelName;
    private TextInputLayout inputCandidateName;
    private TextInputLayout inputCandidateEmail;
    private TextInputLayout inputTechnology;
    private StringBuffer stringBuffer = new StringBuffer();

    private static Retrofit retrofit = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatesinfo);
        setToolbar(getString(R.string.activity_title_candidate_details), false);
        initializeView();
    }

    private void initializeView()
    {
        String interviewDate = formatDate();
        inputPanelName = findViewById(R.id.inputLayoutPanelName);
        inputCandidateName = findViewById(R.id.inputLayoutCandidateName);
        inputCandidateEmail = findViewById(R.id.inputLayoutCandidateEmail);
        inputTechnology = findViewById(R.id.inputLayoutTechnologyTested);
        panelistName = findViewById(R.id.interviewerName);
        panelistName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String panel = panelistName.getText().toString();
                if(!hasFocus && (panel == null || !(panel.matches(AppConstants.VALID_TEXT_PATTERN)))) {
                    String message = getString(R.string.error_panelName);
                    inputPanelName.setError(message);
                    panelistName.setBackgroundResource(R.drawable.edit_text_bg_error);
                }else
                {
                    inputPanelName.setError(null);
                    panelistName.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputPanelName.setErrorEnabled(false);;
                }
            }
        });
        candidatesName = findViewById(R.id.candidateName);
        candidatesName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String candidateName = candidatesName.getText().toString();
                if(!hasFocus && (candidateName == null || !(candidateName.matches(AppConstants.VALID_TEXT_PATTERN)))) {
                    String message = getString(R.string.error_candidateName);
                    inputCandidateName.setError(message);
                    candidatesName.setBackgroundResource(R.drawable.edit_text_bg_error);
                }else
                {
                    inputCandidateName.setError(null);
                    candidatesName.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputCandidateName.setErrorEnabled(false);;
                }
            }
        });
        candidateEmailId = findViewById(R.id.candidateEmail);
        candidateEmailId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String candidateEmail = candidateEmailId.getText().toString();
                if(!hasFocus && (candidateEmail == null || !(candidateEmail.matches(AppConstants.VALID_EMAIL_PATTERN)))) {
                    String message = getString(R.string.error_candidateEmail);
                    inputCandidateEmail.setError(message);
                    candidateEmailId.setBackgroundResource(R.drawable.edit_text_bg_error);
                }else
                {
                    inputCandidateEmail.setError(null);
                    candidateEmailId.setBackgroundResource(R.drawable.edit_text_bg_selector);
                    inputCandidateEmail.setErrorEnabled(false);
                }
            }
        });
        interviewTime = findViewById(R.id.interviewDate);
        interviewTime.setText("Interview Date : "+interviewDate);
        technology = findViewById(R.id.technologyTested);
        enableAutoSuggest();
        submitButton = findViewById(R.id.submit_Button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCandidateDetails();
            }
        });
    }

    public void connectAndGetApiData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL + "technologies/all")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        TechnologyService movieApiService = retrofit.create(TechnologyService.class);
        Call<List<Technology>> call = movieApiService.getAllTechnology();
        call.enqueue(new Callback<List<Technology>>() {
            @Override
            public void onResponse(Call<List<Technology>> call, Response<List<Technology>> response) {
                List<Technology> technologies = response.body();
            }

            @Override
            public void onFailure(Call<List<Technology>> call, Throwable throwable) {
                Log.e(AppConstants.TAG, throwable.toString());
            }
        });
    }

    private void enableAutoSuggest()
    {
        String[] technologyList = getApplicationContext().getResources().getStringArray(R.array.technology);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, technologyList);
        technology.setThreshold(0);
        technology.setAdapter(adapter);
    }

    private boolean submitCandidateDetails() {
        boolean submittedSuccessfully = false;
        String panel = panelistName.getText().toString();
        String candidateName = candidatesName.getText().toString();
        String candidateEmail = candidateEmailId.getText().toString();
        String technologyTested = technology.getText().toString();
        String interviewDate = interviewTime.getText().toString();
        CandidateDetails candidateDetails = new CandidateDetails();
        candidateDetails.setCandidateEmail(candidateEmail);
        candidateDetails.setCandidateName(candidateName);
        candidateDetails.setInterviewerName(panel);
        candidateDetails.setTechnologyTested(technologyTested);
        if(interviewDate != null && !(interviewDate.equals("")))
        {
            String[] strArray = interviewDate.split(":");
            String realDate = strArray[1].trim();
            candidateDetails.setInterviewDate(new Date(realDate));
        }


        if(validateCandidateDetails(candidateDetails)) {
            saveCandidateDetails(candidateDetails);
        }
        else
        {
            Log.d("Input Error", "Please insert valid candidate data");
        }

        return  submittedSuccessfully;
    }

    private boolean validateCandidateDetails(CandidateDetails candidateDetails)
    {
        if (candidateDetails.getInterviewerName() != null && !candidateDetails.getInterviewerName().isEmpty()) {
            inputPanelName.setError(null);
            panelistName.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputPanelName.setErrorEnabled(false);
            panelistName.clearFocus();
        } else {
            String message = getString(R.string.error_panelName);
            inputPanelName.setError(message);
            panelistName.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }
        if (candidateDetails.getCandidateName() != null && !candidateDetails.getCandidateName().isEmpty()) {
            inputCandidateName.setError(null);
            candidatesName.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputCandidateName.setErrorEnabled(false);
            candidatesName.clearFocus();
        } else {
            String message = getString(R.string.error_candidateName);
            inputCandidateName.setError(message);
            candidatesName.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }
        if (candidateDetails.getCandidateEmail() != null && !candidateDetails.getCandidateEmail().isEmpty()) {
            inputCandidateEmail.setError(null);
            candidateEmailId.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputCandidateEmail.setErrorEnabled(false);
            candidateEmailId.clearFocus();
        } else {
            String message = getString(R.string.error_candidateEmail);
            inputCandidateEmail.setError(message);
            candidateEmailId.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        if (candidateDetails.getTechnologyTested() != null && !candidateDetails.getTechnologyTested().isEmpty()) {
            inputTechnology.setError(null);
            technology.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputTechnology.setErrorEnabled(false);
            technology.clearFocus();
        } else {
            String message = getString(R.string.error_technology);
            inputTechnology.setError(message);
            technology.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }
        return  true;
    }

    private String formatDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(new Date());
        return  formattedDate;
    }

    private void saveCandidateDetails(CandidateDetails candidateDetails) {
        CandidateDetailsAsyncTask task = new CandidateDetailsAsyncTask();
        task.execute(candidateDetails);
    }

    private class CandidateDetailsAsyncTask extends AsyncTask<CandidateDetails,Void,Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(CandidateDetails... param) {
            try {
                writeDetailsOnFile(param[0]);
            } catch (IOException e) {
                e.printStackTrace();
                this.exception = e;
                Log.d("Error","Error in persisting candidate's data");
            }
            return null;
        }

        private void writeDetailsOnFile(CandidateDetails candidateDetails) throws IOException {
            FileOutputStream fileOutputStream = null;
            File file = new File(getApplicationContext().getFilesDir(),AppConstants.CANDIDATES_INFO_FILE_PATH);
            if(!(file.exists()) || file.isDirectory())
            {
                stringBuffer.append(AppConstants.CANDIDATE_DETAILS_FILE_HEADER);
            }else
            {
                Log.d("File Not Found Error : ", AppConstants.CANDIDATES_INFO_FILE_PATH+" not found!!");
            }
            try {
                fileOutputStream  = getApplicationContext().openFileOutput(AppConstants.CANDIDATES_INFO_FILE_PATH, Context.MODE_APPEND);
                    stringBuffer.append("\n");
                    stringBuffer.append(candidateDetails.getInterviewerName());
                    stringBuffer.append(" | ");
                    stringBuffer.append(candidateDetails.getCandidateName());
                    stringBuffer.append(" | ");
                    stringBuffer.append(candidateDetails.getCandidateEmail());
                    stringBuffer.append(" | ");
                    stringBuffer.append(candidateDetails.getTechnologyTested());
                    stringBuffer.append(" | ");
                    stringBuffer.append(candidateDetails.getInterviewDate());
                    fileOutputStream.write(stringBuffer.toString().getBytes());
                    stringBuffer.setLength(0);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fileOutputStream != null)
                {
                    fileOutputStream.close();
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (exception == null) {
                navigateToTopicScreen();
            }
        }
    }

    private void navigateToTopicScreen() {
        Intent intent = new Intent(this, TopicsActivity.class);
        intent.putExtra(AppConstants.KEY_TECHNOLOGY, technology.getText().toString());
        startActivity(intent);
        CandidateDetailsActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }
}

interface TechnologyService {
    @GET("group/{id}/users")
    Call<List<Technology>> getAllTechnology();
}
