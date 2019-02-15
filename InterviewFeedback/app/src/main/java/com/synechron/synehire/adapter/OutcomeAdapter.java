package com.synechron.synehire.adapter;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synechron.synehire.R;
import com.synechron.synehire.activity.DiscussionDetailsActivity;
import com.synechron.synehire.ws.response.DiscussionOutcome;

import java.util.List;

public class OutcomeAdapter extends RecyclerView.Adapter<OutcomeAdapter.ViewHolder> {
    private List<DiscussionOutcome> discussionOutcomes;
    private Context context;


    public OutcomeAdapter(Context context, List<DiscussionOutcome> keywords){
        this.context = context;
        this.discussionOutcomes = keywords;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView textViewKeyword;

        ViewHolder(View v){
            super(v);
            cardView = v.findViewById(R.id.card_view);
            textViewKeyword = v.findViewById(R.id.text_view_keyword);
        }
    }

    @Override
    public OutcomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.outcome_keywords, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        DiscussionOutcome keyword = discussionOutcomes.get(position);
        holder.textViewKeyword.setText(keyword.getOutcome() + " ");
        holder.cardView.setCardBackgroundColor(ActivityCompat.getColor(context, R.color.color_gray));
        holder.textViewKeyword.setTextColor(ActivityCompat.getColor(context, R.color.color_black));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCommentRemoval(position);
            }
        });
    }

    private void handleCommentRemoval(int position) {
        DiscussionOutcome discussionOutcome = discussionOutcomes.get(position);
        discussionOutcomes.remove(position);
        notifyItemRemoved(position);
        int size = discussionOutcomes.size();
        notifyItemRangeChanged(position, size);
        ((DiscussionDetailsActivity)context).addOutcomeToSuggestion(discussionOutcome);
    }

    @Override
    public int getItemCount(){
        return discussionOutcomes.size();
    }
}