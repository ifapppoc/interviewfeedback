package com.synechron.synehire.adapter;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synechron.synehire.R;
import com.synechron.synehire.domain.RecommendationRow;

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
        private TextInputLayout inputLayoutComment;
        private EditText comment;
        public CommentEditTextListener addTextChangedListener;

        ViewHolder(View v, CommentEditTextListener addTextChangedListener){
            super(v);
            rlRecommendationSection = v.findViewById(R.id.rlRecommendationSection);
            recommendation = v.findViewById(R.id.textView);
            icon = v.findViewById(R.id.imageViewCollapseIcon);
            llCommentSection = v.findViewById(R.id.llCommentSection);
            inputLayoutComment = v.findViewById(R.id.inputLayoutComment);
            comment = v.findViewById(R.id.editTextComment);
            this.addTextChangedListener = addTextChangedListener;
            comment.addTextChangedListener(addTextChangedListener);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.recommendation_list_item, parent,false);
        return new ViewHolder(v, new CommentEditTextListener());
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
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String comments = holder.comment.getText().toString();
                    if (!comments.isEmpty()) {
                        holder.inputLayoutComment.setError(null);
                        holder.inputLayoutComment.setErrorEnabled(false);
                        holder.comment.setBackgroundResource(R.drawable.edit_text_bg_selector);
                        holder.comment.clearFocus();
                        holder.rlRecommendationSection.performClick();
                    } else {
                        String message = context.getString(R.string.error_enter_recommendation_comments);
                        holder.inputLayoutComment.setError(message);
                        holder.comment.setBackgroundResource(R.drawable.edit_text_bg_error);
                    }
                    return true;
                }
                return false;
            }
        });

        holder.addTextChangedListener.updatePosition(holder.getAdapterPosition());
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

    private class CommentEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            RecommendationRow recommendationRow = recommendations.get(position);
            if (recommendationRow != null) {
                recommendationRow.setComment(charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}