package com.synechrone.interviewfeedback.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.synechrone.interviewfeedback.R;
import com.synechrone.interviewfeedback.ws.response.DiscussionOutcome;
import com.synechrone.interviewfeedback.ws.response.InterviewLevel;
import com.synechrone.interviewfeedback.ws.response.InterviewMode;
import com.synechrone.interviewfeedback.ws.response.Employee;
import com.synechrone.interviewfeedback.ws.response.SubTopic;
import com.synechrone.interviewfeedback.ws.response.Technology;
import com.synechrone.interviewfeedback.ws.response.Topic;

import java.util.List;

public class SuggestionAdapter<T> extends ArrayAdapter<T> {

    private List<T> list;
    private LayoutInflater inflater;

    public SuggestionAdapter(Context context, List<T> list) {
        super(context, R.layout.list_item, R.id.text1, list);
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (rowView == null) {
            rowView = inflater.inflate(R.layout.list_item, parent,false);
        }

        T currentItem = getItem(position);

        String label = "";
        if (currentItem instanceof Technology) {
            label = ((Technology) currentItem).getTechnologyName();
        } else if (currentItem instanceof Topic) {
            label = ((Topic) currentItem).getTopicName();
        } else if (currentItem instanceof SubTopic) {
            label = ((SubTopic) currentItem).getSubTopicName();
        } else if (currentItem instanceof Employee) {
            label = ((Employee) currentItem).getFirstName() + " " + ((Employee) currentItem).getLastName();
        } else if (currentItem instanceof InterviewLevel) {
            label = ((InterviewLevel) currentItem).getLevelName();
        } else if (currentItem instanceof InterviewMode) {
            label = ((InterviewMode) currentItem).getModeName();
        } else if (currentItem instanceof DiscussionOutcome) {
            label = ((DiscussionOutcome) currentItem).getOutcome();
        }

        TextView textView = rowView.findViewById(R.id.text1);
        textView.setText(label);

        return rowView;
    }
}
