package com.synechrone.interviewfeedback.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.adapter.CommentsAndOutcomeAdapter;
import com.synechrone.interviewfeedback.adapter.KeywordAdapter;
import com.synechrone.interviewfeedback.adapter.TopicsAdaptor;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.InterviewSummary;
import com.synechrone.interviewfeedback.uimodel.Keyword;

import java.util.ArrayList;
import java.util.List;

public class InterviewOutcomeActivity extends BaseActivity {
    private RelativeLayout rlParent;
    private CheckBox checkboxDirect;
    private CheckBox checkboxScenario;
    private CheckBox checkboxProject;
    private Button buttonOutcome;
    private LinearLayout llOutcomeSection;
    private RecyclerView recyclerView;

    private String mainTopic;
    private String subTopic;

    private List<InterviewSummary> summaryList;
    private List<Keyword> selectedKeywords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_outcome);
        setToolbar(getString(R.string.activity_title_discussion), true);
        initializeView();
    }

    private void initializeView() {
        rlParent = findViewById(R.id.rlParent);
        TextView textViewHeading = findViewById(R.id.textView_heading);
        checkboxDirect = findViewById(R.id.checkbox_direct);
        checkboxScenario = findViewById(R.id.checkbox_scenario);
        checkboxProject = findViewById(R.id.checkbox_project);
        buttonOutcome = findViewById(R.id.button_outcome);
        buttonOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOutcomeKeywords();
            }
        });
        llOutcomeSection = findViewById(R.id.ll_outcome);
        recyclerView = findViewById(R.id.recycler_view);
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

            List<InterviewSummary> summaryModelList = extras.getParcelableArrayList(AppConstants.KEY_INTERVIEW_SUMMARIES);
            if (summaryModelList != null && summaryModelList.size() > 0) {
                this.summaryList = summaryModelList;
            }
        }
    }

    private void updateCommentsAndOutcomes(List<Keyword> keywords) {
        buttonOutcome.setVisibility(View.GONE);
        llOutcomeSection.setVisibility(View.VISIBLE);
        selectedKeywords.clear();
        selectedKeywords = keywords;
        CommentsAndOutcomeAdapter commentsAndOutcomeAdapter = new CommentsAndOutcomeAdapter(this, selectedKeywords);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commentsAndOutcomeAdapter);
    }

    public void showOutcomeButton() {
        buttonOutcome.setVisibility(View.VISIBLE);
        llOutcomeSection.setVisibility(View.GONE);
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
        interviewSummary.setOutcomeAndComments("fghgf");

        return interviewSummary;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_summary, menu);
        MenuItem item = menu.findItem(R.id.action_summary);
        item.setVisible(summaryList != null && summaryList.size() > 0);
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
        LayoutInflater inflater = (LayoutInflater) InterviewOutcomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_summary_layout,null);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        TopicsAdaptor tAdapter = new TopicsAdaptor(summaryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(tAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        int dialogWindowWidth = (int) (displayWidth * 0.80f);
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

    private void showOutcomeKeywords(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_keywords_layout,null);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        ImageButton buttonDone = layout.findViewById(R.id.button_done);
        String[] keywords = {
                "adkjoasfjqofj","akskjdfqwf","dw","awrwtgegjh awfiwhgi","askhfi hsbd sjfbsf","ajshfdjafsf huagfj hghgas ahggwvd","Crimson",
                "Red","Green","Blue","Yellow","Magenta","Cyan","Orange",
                "Aqua","Azure","Beige","Bisque","Brown","Coral","Crimson"
        };

        List<Keyword> keywordList = new ArrayList<>();
        for (String keyword : keywords) {
            keywordList.add(new Keyword(keyword));
        }
        final KeywordAdapter keywordsAdapter = new KeywordAdapter(this, keywordList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(keywordsAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        int dialogWindowWidth = (int) (displayWidth * 0.80f);
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

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                List<Keyword> selectedKeywords = keywordsAdapter.getSelectedKeywords();
                updateCommentsAndOutcomes(selectedKeywords);
            }
        });
    }
}
