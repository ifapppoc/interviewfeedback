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
import com.synechron.synehire.activity.RecruiterActionActivity;
import com.synechron.synehire.ws.response.EmailId;

import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    private List<EmailId> emailIds;
    private Context context;


    public EmailAdapter(Context context, List<EmailId> emails){
        this.context = context;
        this.emailIds = emails;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.outcome_keywords, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        EmailId emailId = emailIds.get(position);
        holder.textViewKeyword.setText(emailId.getEmailId());
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
        EmailId emailId = emailIds.get(position);
        emailIds.remove(position);
        notifyItemRemoved(position);
        int size = emailIds.size();
        notifyItemRangeChanged(position, size);
        ((RecruiterActionActivity)context).addEmailToSuggestion(emailId);
    }

    @Override
    public int getItemCount(){
        return emailIds.size();
    }
}