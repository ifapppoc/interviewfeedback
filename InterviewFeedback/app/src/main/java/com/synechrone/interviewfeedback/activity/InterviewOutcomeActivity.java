package com.synechrone.interviewfeedback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.constants.AppConstants;

public class InterviewOutcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_outcome);
        setToolbar(getString(R.string.activity_title_discussion));
        initializeView();
    }

    private void initializeView() {
        TextView textViewHeading = findViewById(R.id.textView_heading);
        CheckedTextView checkboxDirect = findViewById(R.id.checkbox_direct);
        CheckedTextView checkboxScenario = findViewById(R.id.checkbox_scenario);
        CheckedTextView checkboxProject = findViewById(R.id.checkbox_project);
        Button buttonContinue = findViewById(R.id.button_continue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueInterview();
            }
        });

        Button buttonFinish = findViewById(R.id.button_finish);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishInterview();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            String mainTopic = extras.getString(AppConstants.KEY_MAIN_TOPIC);
            String subTopic = extras.getString(AppConstants.KEY_SUB_TOPIC);
            textViewHeading.setText(mainTopic + " >> " + subTopic);
        }
    }

    private void finishInterview() {
    }

    private void continueInterview() {

    }
}
