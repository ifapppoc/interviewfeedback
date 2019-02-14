package com.synechrone.synehire.adapter;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synechrone.synehire.R;
import com.synechrone.synehire.domain.DiscussionOutcomesRow;
import com.synechrone.synehire.ws.response.DiscussionOutcome;

import java.util.ArrayList;
import java.util.List;

public class SummaryOutcomeAdapter extends RecyclerView.Adapter<SummaryOutcomeAdapter.ViewHolder> {
    private List<DiscussionOutcomesRow> outcomes;
    private Context context;

    public SummaryOutcomeAdapter(Context context, List<DiscussionOutcomesRow> outcomes){
        this.context = context;
        this.outcomes = outcomes;
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
    public SummaryOutcomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.outcome_keywords, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        DiscussionOutcomesRow outcomesRow = outcomes.get(position);
        holder.textViewKeyword.setText(outcomesRow.getOutcome() + " ");
        holder.cardView.setCardBackgroundColor(ActivityCompat.getColor(context, R.color.color_white));
        holder.textViewKeyword.setTextColor(ActivityCompat.getColor(context, R.color.color_black));
        boolean isSelected = outcomesRow.isSelected();
        holder.textViewKeyword.setCompoundDrawablesWithIntrinsicBounds(0, 0, (isSelected ? R.drawable.icon_close : 0), 0);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiscussionOutcomesRow discussionOutcome = outcomes.get(position);
                boolean isSelected = discussionOutcome.isSelected();
                holder.textViewKeyword.setCompoundDrawablesWithIntrinsicBounds(0, 0, (isSelected ? 0 : R.drawable.icon_close), 0);
                discussionOutcome.setSelected(!isSelected);
            }
        });
    }

    @Override
    public int getItemCount(){
        return outcomes.size();
    }

    public List<DiscussionOutcome> getSelectedOutcomes() {
        List<DiscussionOutcome> selectedOutcomes = new ArrayList<>();
        for (DiscussionOutcomesRow discussionOutcome : outcomes) {
            if (discussionOutcome.isSelected()) {
                selectedOutcomes.add(new DiscussionOutcome(discussionOutcome.getId(), discussionOutcome.getOutcome()));
            }
        }
        return selectedOutcomes;
    }
}