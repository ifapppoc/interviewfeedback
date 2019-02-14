package com.synechrone.interviewfeedback.adapter;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.domain.RecommendationRow;
import com.synechrone.interviewfeedback.ws.response.EmailId;
import com.synechrone.interviewfeedback.ws.response.Recommendation;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
    private List<RecommendationRow> recommendations;
    private Context context;

    public RecommendationAdapter(Context context, List<RecommendationRow> recommendations){
        this.context = context;
        this.recommendations = recommendations;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlRecommendationSection;
        private TextView recommendation;
        private ImageView icon;
        private LinearLayout llCommentSection;
        private EditText comment;

        ViewHolder(View v){
            super(v);
            rlRecommendationSection = v.findViewById(R.id.rlRecommendationSection);
            recommendation = v.findViewById(R.id.textView);
            icon = v.findViewById(R.id.imageViewCollapseIcon);
            llCommentSection = v.findViewById(R.id.llCommentSection);
            comment = v.findViewById(R.id.editTextComment);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.recommendation_list_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        RecommendationRow recommendationRow = recommendations.get(position);
        String label = recommendationRow != null ? recommendationRow.getRecommendation() : null;
        holder.recommendation.setText(label);
        holder.icon.setImageResource(R.drawable.arrow_down);

        holder.comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String comments = holder.comment.getText().toString();
                    RecommendationRow recommendationRow = recommendations.get(holder.getAdapterPosition());
                    if (!comments.isEmpty()) {
                        recommendationRow.setComment(comments);
                        holder.rlRecommendationSection.callOnClick();
                    }
                    return true;
                }
                return false;
            }
        });

        holder.rlRecommendationSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendationRow recommendationRow = recommendations.get(holder.getAdapterPosition());
                boolean isCollapsed = recommendationRow.isCollapsed();
                holder.icon.setImageResource(isCollapsed ? R.drawable.arrow_up : R.drawable.arrow_down);
                holder.llCommentSection.setVisibility(isCollapsed ? View.VISIBLE : View.GONE);
                recommendationRow.setCollapsed(!isCollapsed);
            }
        });
    }

    @Override
    public int getItemCount(){
        return recommendations.size();
    }

    public List<RecommendationRow> getRecommendations() {
        return recommendations;
    }
}