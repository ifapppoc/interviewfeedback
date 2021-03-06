package com.synechron.synehire.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.text.HtmlCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synechron.synehire.R;
import com.synechron.synehire.domain.DiscussionOutcomesRow;
import com.synechron.synehire.ws.response.DiscussionMode;
import com.synechron.synehire.ws.response.DiscussionOutcome;
import com.synechron.synehire.ws.response.InterviewSummary;

import java.util.ArrayList;
import java.util.List;

public class InterviewSummaryAdaptor extends RecyclerView.Adapter<InterviewSummaryAdaptor.MyViewHolder> {
    private List<InterviewSummary> interviewSummaries;
    private List<DiscussionOutcome> outcomes;
    private Context context;
    private boolean isEditable;
    private boolean comingFromRecommendation;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parent;
        private TextView topics;
        private TextView modeOfDiscussion;
        private TextView outcomes;
        private TextView comments;
        private View divider;

        MyViewHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.llParent);
            topics = view.findViewById(R.id.textViewTopicWithSubtopics);
            modeOfDiscussion = view.findViewById(R.id.textViewModeOfDiscussion);
            outcomes = view.findViewById(R.id.textViewOutcome);
            comments = view.findViewById(R.id.textViewComment);
            divider = view.findViewById(R.id.divider);
        }
    }

    public InterviewSummaryAdaptor(Context  context, List<InterviewSummary> summaryModelList, List<DiscussionOutcome> outcomes, boolean comingFromRecommendation) {
        this.interviewSummaries = summaryModelList;
        this.outcomes = outcomes;
        this.context = context;
        isEditable = outcomes != null && outcomes.size() > 0;
        this.comingFromRecommendation = comingFromRecommendation;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_summary_topics, parent, false);
        return new InterviewSummaryAdaptor.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        InterviewSummary interviewSummary = interviewSummaries.get(position);
        holder.parent.setBackgroundColor(isEditable ? ActivityCompat.getColor(context, R.color.color_white) : (comingFromRecommendation ? ActivityCompat.getColor(context, R.color.color_white) : ActivityCompat.getColor(context, R.color.color_gray)));
        holder.topics.setText(interviewSummary.getTopic() + ": " + interviewSummary.getSubTopic());
        String commentStr = interviewSummary.getComment();
        if (commentStr != null && !commentStr.isEmpty()) {
            holder.comments.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.comments.setText(HtmlCompat.fromHtml("<b>Comment: </b>" + commentStr, HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                holder.comments.setText(Html.fromHtml("<b>Comment: </b>" + commentStr));
            }
        } else {
            holder.comments.setVisibility(View.GONE);
        }

        List<DiscussionMode> discussionModes = interviewSummary.getDiscussionModes();
        String mode = "<b>Mode: </b>";
        if (discussionModes != null && discussionModes.size() > 0) {
            for (DiscussionMode discussionMode : discussionModes) {
                mode += discussionMode.getModeName() + ", ";
            }
        }
        List<DiscussionOutcome> discussionOutcomes = interviewSummary.getDiscussionOutcomes();
        String feedback = "<b>Feedback: </b>";
        if (discussionOutcomes != null && discussionOutcomes.size() > 0) {
            for (DiscussionOutcome discussionOutcome : discussionOutcomes) {
                feedback += discussionOutcome.getOutcome() + ", ";
            }
        }

        holder.modeOfDiscussion.setText(mode.substring(0, mode.length()-2));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.modeOfDiscussion.setText(HtmlCompat.fromHtml(mode.substring(0, mode.length()-2), HtmlCompat.FROM_HTML_MODE_LEGACY));
        } else {
            holder.modeOfDiscussion.setText(Html.fromHtml(mode.substring(0, mode.length()-2)));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.outcomes.setText(HtmlCompat.fromHtml(feedback.substring(0, feedback.length()-2), HtmlCompat.FROM_HTML_MODE_LEGACY));
        } else {
            holder.outcomes.setText(Html.fromHtml(feedback.substring(0, feedback.length()-2)));
        }
        holder.outcomes.setCompoundDrawablesWithIntrinsicBounds(0, 0, (isEditable ? R.drawable.icon_edit : 0), 0);
        holder.outcomes.setOnClickListener(isEditable ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.parent.setBackgroundColor(ActivityCompat.getColor(context, R.color.color_light_yellow));
                handleCommentEditing(holder.outcomes, position);
            }
        } : null);
        holder.divider.setVisibility((position == getItemCount() - 1) ? View.GONE : View.VISIBLE);
    }

    private void handleCommentEditing(View view, int position) {
        InterviewSummary interviewSummary = interviewSummaries.get(position);
        List<DiscussionOutcomesRow> outcomesRows = new ArrayList<>();
        List<DiscussionOutcome> selectedOutcomes = interviewSummary.getDiscussionOutcomes();
        for (DiscussionOutcome selectedOutcome : selectedOutcomes) {
            outcomesRows.add(new DiscussionOutcomesRow(selectedOutcome.getId(), selectedOutcome.getOutcome(), true));
        }

        for (DiscussionOutcome outcome : outcomes) {
            if (!selectedOutcomes.contains(outcome)) {
                outcomesRows.add(new DiscussionOutcomesRow(outcome.getId(), outcome.getOutcome(), false));
            }
        }
        showSummary(view, outcomesRows, position);
    }

    private void showSummary(View view, List<DiscussionOutcomesRow> outcomesRows, final int position){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_summary_layout,null);
        RelativeLayout rlBottomSection = layout.findViewById(R.id.rlBottomSection);
        rlBottomSection.setVisibility(View.VISIBLE);
        ImageView imageViewDone = layout.findViewById(R.id.imageViewDone);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        final SummaryOutcomeAdapter summaryOutcomeAdapter = new SummaryOutcomeAdapter(context, outcomesRows);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(summaryOutcomeAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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

        imageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
                updateOutcome(summaryOutcomeAdapter.getSelectedOutcomes(), position);
            }
        });
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void updateOutcome(List<DiscussionOutcome> selectedOutcomes, int position) {
        InterviewSummary interviewSummary = interviewSummaries.get(position);
        interviewSummary.setDiscussionOutcomes(selectedOutcomes);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return interviewSummaries.size();
    }
}