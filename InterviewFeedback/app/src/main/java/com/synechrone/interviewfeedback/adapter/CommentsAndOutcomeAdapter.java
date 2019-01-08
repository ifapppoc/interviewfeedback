package com.synechrone.interviewfeedback.adapter;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.activity.InterviewOutcomeActivity;
import com.synechrone.interviewfeedback.uimodel.Keyword;

import java.util.List;

public class CommentsAndOutcomeAdapter extends RecyclerView.Adapter<CommentsAndOutcomeAdapter.ViewHolder> {
    private List<Keyword> keywords;
    private Context context;

    public CommentsAndOutcomeAdapter(Context context, List<Keyword> keywords){
        this.context = context;
        this.keywords = keywords;
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
    public CommentsAndOutcomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.outcome_keywords,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Keyword keyword = keywords.get(position);
        holder.textViewKeyword.setText(keyword.getKeyword());
        holder.cardView.setCardBackgroundColor(ActivityCompat.getColor(context, R.color.color_gray));
        holder.textViewKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCommentRemoval(position);
            }
        });
    }

    private void handleCommentRemoval(int position) {
        keywords.remove(position);
        notifyItemRemoved(position);
        int size = keywords.size();
        notifyItemRangeChanged(position, size);
        if (size == 0) {
            ((InterviewOutcomeActivity)context).showOutcomeButton();
        }
    }

    @Override
    public int getItemCount(){
        return keywords.size();
    }
}