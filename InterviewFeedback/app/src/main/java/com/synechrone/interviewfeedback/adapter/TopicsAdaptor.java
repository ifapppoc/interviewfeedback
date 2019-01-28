package com.synechrone.interviewfeedback.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.domain.InterviewSummary;
import com.synechrone.interviewfeedback.utility.PrefManager;

import java.util.List;

public class TopicsAdaptor extends RecyclerView.Adapter<TopicsAdaptor.MyViewHolder> {
    private List<InterviewSummary> summaryModelList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView topics, modeOfDiscussion, comments;
        private View divider;
        private ImageView editCommentsButton;

        MyViewHolder(View view) {
            super(view);
            topics = view.findViewById(R.id.textViewTopicWithSubtopics);
            modeOfDiscussion = view.findViewById(R.id.textViewModeOfDiscussion);
            comments = view.findViewById(R.id.text_comments);
            divider = view.findViewById(R.id.divider);
            editCommentsButton=view.findViewById(R.id.text_editCommentsButton);
        }
    }

    public TopicsAdaptor(List<InterviewSummary> summaryModelList, Context  context) {
        this.summaryModelList = summaryModelList;
        this.context=context;
    }

    @Override
    public TopicsAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_summary_topics, parent, false);

        return new TopicsAdaptor.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopicsAdaptor.MyViewHolder holder, final int position) {
        InterviewSummary summaryModel = summaryModelList.get(position);
        holder.topics.setText(summaryModel.getMainTopic() + ": " + summaryModel.getSubTopic());
        holder.modeOfDiscussion.setText("Mode: "+summaryModel.getModeOfDiscussion());
        /*holder.comments.setText("Feedback: "+summaryModel.getOutcomeAndComments());*/
        String summary=summaryModel.getOutcomeAndComments();
        if(summary.startsWith(",")){
            summary=summary.substring(1,summary.length());
        }
        holder.comments.setText("Feedback: "+summary);
        holder.divider.setVisibility((position == getItemCount() - 1) ? View.GONE : View.VISIBLE);
        holder.editCommentsButton.setVisibility(View.VISIBLE);
        holder.editCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCommentBox(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return summaryModelList.size();
    }

    private void openCommentBox(final int position){
        final InterviewSummary interviewSummary=summaryModelList.get(position);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Comments");
        alertDialog.setMessage("Edit Feedback");

        final EditText input = new EditText(context);
        input.setText(interviewSummary.getOutcomeAndComments());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Submit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        interviewSummary.setOutcomeAndComments(input.getText().toString());
                        notifyDataSetChanged();
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }





}