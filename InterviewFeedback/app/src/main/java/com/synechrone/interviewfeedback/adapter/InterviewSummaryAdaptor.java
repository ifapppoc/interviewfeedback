package com.synechrone.interviewfeedback.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.ws.response.DiscussionMode;
import com.synechrone.interviewfeedback.ws.response.DiscussionOutcome;
import com.synechrone.interviewfeedback.ws.response.InterviewSummary;

import java.util.List;

public class InterviewSummaryAdaptor extends RecyclerView.Adapter<InterviewSummaryAdaptor.MyViewHolder> {
    private List<InterviewSummary> summaryModelList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView topics, modeOfDiscussion, comments;
        private View divider;

        MyViewHolder(View view) {
            super(view);
            topics = view.findViewById(R.id.textViewTopicWithSubtopics);
            modeOfDiscussion = view.findViewById(R.id.textViewModeOfDiscussion);
            comments = view.findViewById(R.id.text_comments);
            divider = view.findViewById(R.id.divider);
        }
    }

    public InterviewSummaryAdaptor(List<InterviewSummary> summaryModelList, Context  context) {
        this.summaryModelList = summaryModelList;
    }

    @Override
    public InterviewSummaryAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_summary_topics, parent, false);
        return new InterviewSummaryAdaptor.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InterviewSummaryAdaptor.MyViewHolder holder, final int position) {
        InterviewSummary interviewSummary = summaryModelList.get(position);
        holder.topics.setText(interviewSummary.getTopic());
        List<DiscussionMode> discussionModes = interviewSummary.getDiscussionModes();
        String mode = "Mode: ";
        if (discussionModes != null && discussionModes.size() > 0) {
            for (DiscussionMode discussionMode : discussionModes) {
                mode += discussionMode.getModeName();
            }
        }
        List<DiscussionOutcome> discussionOutcomes = interviewSummary.getDiscussionOutcomes();
        String feedback = "Feedback: ";
        if (discussionOutcomes != null && discussionOutcomes.size() > 0) {
            for (DiscussionOutcome discussionOutcome : discussionOutcomes) {
                feedback += discussionOutcome.getOutcome();
            }
        }
        holder.modeOfDiscussion.setText(mode);
        holder.comments.setText(feedback);
        holder.divider.setVisibility((position == getItemCount() - 1) ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return summaryModelList.size();
    }
}