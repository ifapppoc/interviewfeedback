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
import com.synechrone.interviewfeedback.uimodel.Keyword;

import java.util.ArrayList;
import java.util.List;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.ViewHolder> {
    private List<Keyword> keywords;
    private Context context;

    public KeywordAdapter(Context context, List<Keyword> keywords){
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
    public KeywordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.outcome_keywords,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Keyword keyword = keywords.get(position);
        holder.textViewKeyword.setText(keyword.getKeyword());
        boolean isSelected = keyword.isSelected();
        holder.cardView.setCardBackgroundColor(isSelected ? ActivityCompat.getColor(context, R.color.color_black) : ActivityCompat.getColor(context, R.color.color_white));
        holder.textViewKeyword.setTextColor(isSelected ? ActivityCompat.getColor(context, R.color.color_white): ActivityCompat.getColor(context, R.color.color_black));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Keyword keyword = keywords.get(position);
                boolean isSelected = keyword.isSelected();
                keyword.setSelected(!isSelected);
                holder.cardView.setCardBackgroundColor(!isSelected ? ActivityCompat.getColor(context, R.color.color_black) : ActivityCompat.getColor(context, R.color.color_white));
                holder.textViewKeyword.setTextColor(!isSelected ? ActivityCompat.getColor(context, R.color.color_white): ActivityCompat.getColor(context, R.color.color_black));
            }
        });
    }

    @Override
    public int getItemCount(){
        return keywords.size();
    }

    public List<Keyword> getSelectedKeywords() {
        List<Keyword> selectedKeywords = new ArrayList<>();
        for (Keyword keyword : keywords) {
            if (keyword.isSelected()) {
                selectedKeywords.add(keyword);
            }
        }

        return selectedKeywords;
    }
}
