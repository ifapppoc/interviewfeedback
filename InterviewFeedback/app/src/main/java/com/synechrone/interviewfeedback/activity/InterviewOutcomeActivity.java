package com.synechrone.interviewfeedback.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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

import java.util.ArrayList;
import java.util.List;

public class InterviewOutcomeActivity extends BaseActivity {

    private CheckBox checkboxDirect;
    private CheckBox checkboxScenario;
    private CheckBox checkboxProject;
    private TextInputLayout inputOutcome;
    private EditText editTextComments;

    private String mainTopic;
    private String subTopic;

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
        inputOutcome = findViewById(R.id.inputLayout_comments);
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
        boolean isValid = validateInterviewSummary();
        if (isValid) {
            InterviewSummary interviewSummary = prepareInterviewSummary();
            Intent intent = new Intent();
            intent.putExtra(AppConstants.KEY_REQUEST_CODE, requestCode);
            intent.putExtra(AppConstants.KEY_INTERVIEW_SUMMARY, interviewSummary);
            setResult(RESULT_OK, intent);
            this.finish();
            overridePendingTransition(R.anim.slide_in_forward, R.anim.slide_out_forward);
        }
    }

    private boolean validateInterviewSummary() {
        String comments = editTextComments.getText().toString();
        if (!comments.isEmpty()) {
            inputOutcome.setError(null);
            editTextComments.setBackgroundResource(R.drawable.edit_text_bg_selector);
            inputOutcome.setErrorEnabled(false);
            editTextComments.clearFocus();
        } else {
            String message = getString(R.string.error_enter_comments);
            inputOutcome.setError(message);
            editTextComments.setBackgroundResource(R.drawable.edit_text_bg_error);
            return false;
        }

        if (!(checkboxDirect.isChecked() || checkboxScenario.isChecked() || checkboxProject.isChecked())) {
            Toast.makeText(this, getString(R.string.error_select_mode_of_discussion), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
        interviewSummary.setModeOfDiscussion(modeOfDiscussion);
        interviewSummary.setOutcomeAndComments(editTextComments.getText().toString());

        return interviewSummary;
    }
}
