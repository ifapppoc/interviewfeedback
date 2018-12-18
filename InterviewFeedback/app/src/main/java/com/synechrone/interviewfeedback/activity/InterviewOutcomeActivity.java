package com.synechrone.interviewfeedback.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.InterviewSummary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class InterviewOutcomeActivity extends BaseActivity {

    private CheckBox checkboxDirect;
    private CheckBox checkboxScenario;
    private CheckBox checkboxProject;
    private EditText editTextComments;

    private String mainTopic;
    private String subTopic;

    private static final String INTERVIEW_SUMMARY_FILE_PATH = "interviewSummary.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_outcome);
        setToolbar(getString(R.string.activity_title_discussion));
        initializeView();
    }

    private void initializeView() {
        TextView textViewHeading = findViewById(R.id.textView_heading);
        checkboxDirect = findViewById(R.id.checkbox_direct);
        checkboxScenario = findViewById(R.id.checkbox_scenario);
        checkboxProject = findViewById(R.id.checkbox_project);
        editTextComments = findViewById(R.id.editText_comments);
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
                saveInterviewSummary(2);
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            this.mainTopic = extras.getString(AppConstants.KEY_MAIN_TOPIC);
            this.subTopic = extras.getString(AppConstants.KEY_SUB_TOPIC);
            textViewHeading.setText(mainTopic + " >> " + subTopic);
        }
    }

    private void saveInterviewSummary(int requestCode) {
        InterviewSummary interviewSummary = prepareInterviewSummary();
        SaveInterviewSummaryAsyncTask task = new SaveInterviewSummaryAsyncTask(requestCode);
        task.execute(interviewSummary);
    }

    private InterviewSummary prepareInterviewSummary() {
        InterviewSummary interviewSummary = new InterviewSummary();
        interviewSummary.setMainTopic(mainTopic);
        interviewSummary.setSubTopic(subTopic);
        List<String> modeOfDiscussions = new ArrayList<>();
        if (checkboxDirect.isChecked()) {
            modeOfDiscussions.add(getString(R.string.text_direct_theoritical));
        }

        if (checkboxScenario.isChecked()) {
            modeOfDiscussions.add(getString(R.string.text_scenario_based));
        }

        if (checkboxProject.isChecked()) {
            modeOfDiscussions.add(getString(R.string.text_prior_project_based));
        }

        String modeOfDiscussion = TextUtils.join(",", modeOfDiscussions);
        interviewSummary.setOutcomeAndComments(modeOfDiscussion);
        interviewSummary.setOutcomeAndComments(editTextComments.getText().toString());

        return interviewSummary;
    }

    private class SaveInterviewSummaryAsyncTask extends AsyncTask<InterviewSummary, Void, Void> {

        private Exception exception;
        private int requestCode;

        SaveInterviewSummaryAsyncTask(int requestCode) {
            this.requestCode = requestCode;
        }

        @Override
        protected Void doInBackground(InterviewSummary... params) {
            try {
                writeSummaryOnFile(params[0]);
            } catch (IOException e) {
                this.exception = e;
            }
            return null;
        }

        private void writeSummaryOnFile(InterviewSummary interviewSummary) throws IOException {
            try (FileOutputStream fileOutputStream  = getApplicationContext().openFileOutput(INTERVIEW_SUMMARY_FILE_PATH, Context.MODE_PRIVATE);
                 ObjectOutputStream objectOutputStream  = new ObjectOutputStream(fileOutputStream)) {
                 objectOutputStream.writeObject(interviewSummary);
            } catch (IOException e) {
                throw new IOException("Something went wrong while writing summary into file");
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            if (exception == null) {
                navigateToNextScreen(requestCode);
            } else {
                showError(exception.getMessage());
            }
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void navigateToNextScreen(int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.KEY_REQUEST_CODE, requestCode);
        setResult(RESULT_OK, intent);
        this.finish();
        overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
    }
}
