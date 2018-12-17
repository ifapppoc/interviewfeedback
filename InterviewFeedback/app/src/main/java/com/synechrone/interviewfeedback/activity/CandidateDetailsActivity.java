package com.synechrone.interviewfeedback.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.domain.CandidateDetails;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class CandidateDetailsActivity extends AppCompatActivity {

    private EditText panelistName;
    private EditText candidatesName;
    private EditText candidateEmailId;
    private EditText interviewTime;
    private EditText technology;
    private Button submitButton;
    private TextInputLayout inputPanelName;
    private TextInputLayout inputCandidateName;
    private TextInputLayout inputCandidateEmail;
    private TextInputLayout inputTechnology;
    private TextInputLayout inputInterviewDate;

    private static final String CANDIDATES_INFO_FILE_PATH = "appdata/logindata/candidateDetails.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidatesinfo);
        inputPanelName = findViewById(R.id.inputLayoutPanelName);
        inputCandidateName = findViewById(R.id.inputLayoutCandidateName);
        inputCandidateEmail = findViewById(R.id.inputLayoutCandidateEmail);
        inputInterviewDate = findViewById(R.id.inputLayoutInterviewDate);
        inputTechnology = findViewById(R.id.inputLayoutTechnologyTested);
        panelistName = findViewById(R.id.interviewerName);
        candidatesName = findViewById(R.id.candidateName);
        candidateEmailId = findViewById(R.id.candidateEmail);
        interviewTime = findViewById(R.id.interviewDate);
        technology = findViewById(R.id.technologyTested);
        submitButton = findViewById(R.id.submit_Button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean submittedSuccessfully = submitCandidateDetails();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Persistent Error","Error in persisting candidates details");
                }
            }
        });
    }

    private boolean submitCandidateDetails() throws IOException {
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
        candidateDetails.setInterviewDate(new Date(interviewDate));

        if(validateCandidateDetails(candidateDetails)) {
            submittedSuccessfully = writeDetailsOnFile(candidateDetails);
        }
        else
        {
            Log.d("Input Error", "Please insert valid candidate data");
        }

        return  submittedSuccessfully;
    }

    private boolean writeDetailsOnFile(CandidateDetails candidateDetails) throws IOException {
        boolean dataPersisted = false;
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream  = getApplicationContext().openFileOutput(CANDIDATES_INFO_FILE_PATH,Context.MODE_PRIVATE);
            objectOutputStream  = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(candidateDetails);
            dataPersisted = true;
            } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(objectOutputStream != null)
            {
                objectOutputStream.close();
            }
            if(fileOutputStream != null)
            {
                fileOutputStream.close();
            }
        }
        return dataPersisted;
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
        if (candidateDetails.getInterviewDate() != null) {
            inputInterviewDate.setError(null);
            interviewTime.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputInterviewDate.setErrorEnabled(false);
            interviewTime.clearFocus();
        } else {
            String message = getString(R.string.error_interviewDate);
            inputInterviewDate.setError(message);
            interviewTime.setBackgroundResource(R.drawable.edit_text_bg_error);
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


}