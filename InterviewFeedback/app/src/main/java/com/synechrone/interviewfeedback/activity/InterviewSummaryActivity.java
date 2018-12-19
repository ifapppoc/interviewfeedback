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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.constants.AppConstants;
import com.synechrone.interviewfeedback.domain.InterviewSummary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
public class InterviewSummaryActivity extends BaseActivity {

    private StringBuffer stringBuffer = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_summary);
        setToolbar(getString(R.string.activity_summary_title));
        initializeView();
    }

    private void initializeView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        Button buttonSubmitAssessment = findViewById(R.id.submit_assessment);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            final List<InterviewSummary> summaryModelList = extras.getParcelableArrayList(AppConstants.KEY_INTERVIEW_SUMMARIES);
            if (summaryModelList != null && summaryModelList.size() > 0) {
                TopicsAdaptor tAdapter = new TopicsAdaptor(summaryModelList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(tAdapter);

                buttonSubmitAssessment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        writeInterviewSummary(summaryModelList);
                    }
                });
            }
        }
    }

    private class TopicsAdaptor extends RecyclerView.Adapter<TopicsAdaptor.MyViewHolder> {
        private List<InterviewSummary> summaryModelList;

        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView topics, modeOfDiscussion, comments;

            MyViewHolder(View view) {
                super(view);
                topics = view.findViewById(R.id.textViewTopicWithSubtopics);
                modeOfDiscussion = view.findViewById(R.id.textViewModeOfDiscussion);
                comments = view.findViewById(R.id.text_comments);
            }
        }

        TopicsAdaptor(List<InterviewSummary> summaryModelList) {
            this.summaryModelList = summaryModelList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_summary_topics, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            InterviewSummary summaryModel = summaryModelList.get(position);
            holder.topics.setText(summaryModel.getMainTopic() + " >> " + summaryModel.getSubTopic());
            holder.modeOfDiscussion.setText("Mode: "+summaryModel.getModeOfDiscussion());
            holder.comments.setText("Feedback : "+summaryModel.getOutcomeAndComments());
        }

        @Override
        public int getItemCount() {
            return summaryModelList.size();
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
            File file = new File(getApplicationContext().getFilesDir(),AppConstants.DISCUSSION_SUMMARY_FILE);
            if(!(file.exists()) || file.isDirectory())
            {
                stringBuffer.append(AppConstants.INTERVIEW_SUMMARY_FILE_HEADER);
            }else
            {
                Log.d("File Not Found Error : ", AppConstants.DISCUSSION_SUMMARY_FILE+" not found!!");
            }
            try {
                fileOutputStream  = getApplicationContext().openFileOutput(AppConstants.DISCUSSION_SUMMARY_FILE, Context.MODE_APPEND);
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
