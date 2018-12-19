package com.synechrone.interviewfeedback.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.domain.InterviewSummary;

import java.util.List;

public class TopicsAdaptor extends RecyclerView.Adapter<TopicsAdaptor.MyViewHolder> {
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

    public TopicsAdaptor(List<InterviewSummary> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }

    @Override
    public TopicsAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_summary_topics, parent, false);

        return new TopicsAdaptor.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopicsAdaptor.MyViewHolder holder, int position) {
        InterviewSummary summaryModel = summaryModelList.get(position);
        holder.topics.setText(summaryModel.getMainTopic() + " >> " + summaryModel.getSubTopic());
        holder.modeOfDiscussion.setText("Mode: "+summaryModel.getModeOfDiscussion());
        holder.comments.setText("Feedback : "+summaryModel.getOutcomeAndComments());
        holder.divider.setVisibility((position == getItemCount() - 1) ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return summaryModelList.size();
    }
}