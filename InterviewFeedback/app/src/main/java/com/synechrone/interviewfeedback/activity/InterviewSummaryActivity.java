package com.synechrone.interviewfeedback.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.adapter.TopicsAdaptor;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.InterviewSummary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
public class InterviewSummaryActivity extends BaseActivity {

    private static final String DISCUSSION_SUMMARY_FILE = "discussionSummary.txt";
    private static final String FILE_HEADER = "Main Topic | Sub Topic | Mode Of Discussion | Feedback";
    private StringBuffer stringBuffer = new StringBuffer(FILE_HEADER);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_summary);
        setToolbar(getString(R.string.activity_summary_title), false);
        initializeView();
    }

    private void initializeView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        Button buttonSubmitAssessment = findViewById(R.id.submit_assessment);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            final List<InterviewSummary> summaryList = extras.getParcelableArrayList(AppConstants.KEY_INTERVIEW_SUMMARIES);
            if (summaryList != null && summaryList.size() > 0) {
                TopicsAdaptor tAdapter = new TopicsAdaptor(summaryList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(tAdapter);

                buttonSubmitAssessment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        writeInterviewSummary(summaryList);
                    }
                });
            }
        }
    }

    private void writeInterviewSummary(List<InterviewSummary> interviewSummaries) {
        SaveInterviewSummaryAsyncTask task = new SaveInterviewSummaryAsyncTask();
        task.execute(interviewSummaries);
    }

    private class SaveInterviewSummaryAsyncTask extends AsyncTask<List<InterviewSummary>,Void,Void> {

        private Exception exception;

        @Override
        protected Void doInBackground(List<InterviewSummary>... param) {
            try {
                writeDiscussionSummaryToDisk(param[0]);
            } catch (IOException e) {
                e.printStackTrace();
                this.exception = e;
                Log.d("Error","Error in persisting candidate's data");
            }
            return null;
        }

        private void writeDiscussionSummaryToDisk(List<InterviewSummary> summaryList) throws IOException {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream  = getApplicationContext().openFileOutput(DISCUSSION_SUMMARY_FILE, Context.MODE_APPEND);
                for (InterviewSummary interviewSummary: summaryList) {
                    stringBuffer.append("\n");
                    stringBuffer.append(interviewSummary.getMainTopic());
                    stringBuffer.append(" | ");
                    stringBuffer.append(interviewSummary.getSubTopic());
                    stringBuffer.append(" | ");
                    stringBuffer.append(interviewSummary.getModeOfDiscussion());
                    stringBuffer.append(" | ");
                    stringBuffer.append(interviewSummary.getOutcomeAndComments());
                    fileOutputStream.write(stringBuffer.toString().getBytes());
                    stringBuffer.setLength(0);
                }
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
                Toast.makeText(InterviewSummaryActivity.this, "Feedback has been submitted successfully.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
