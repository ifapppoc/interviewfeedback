package com.synechrone.synehire.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.synechrone.synehire.ws.response.DiscussionMode;
import com.synechrone.synehire.ws.response.DiscussionOutcome;

import java.util.List;

public class InterviewSummary implements Parcelable {

    private String topic;
    private String subTopic;
    private String modeOfDiscussion;
    private String outcomeAndComments;
    private List<DiscussionMode> discussionModes;
    private List<DiscussionOutcome> discussionOutcomes;

    public InterviewSummary() { }

    private InterviewSummary(Parcel in) {
        topic = in.readString();
        subTopic = in.readString();
        modeOfDiscussion = in.readString();
        outcomeAndComments = in.readString();
        in.readList(discussionModes, List.class.getClassLoader());
        in.readList(discussionOutcomes, List.class.getClassLoader());
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public String getModeOfDiscussion() {
        return modeOfDiscussion;
    }

    public void setModeOfDiscussion(String modeOfDiscussion) {
        this.modeOfDiscussion = modeOfDiscussion;
    }

    public String getOutcomeAndComments() {
        return outcomeAndComments;
    }

    public void setOutcomeAndComments(String outcomeAndComments) {
        this.outcomeAndComments = outcomeAndComments;
    }

    public List<DiscussionMode> getDiscussionModes() {
        return discussionModes;
    }

    public void setDiscussionModes(List<DiscussionMode> discussionModes) {
        this.discussionModes = discussionModes;
    }

    public List<DiscussionOutcome> getDiscussionOutcomes() {
        return discussionOutcomes;
    }

    public void setDiscussionOutcomes(List<DiscussionOutcome> discussionOutcomes) {
        this.discussionOutcomes = discussionOutcomes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(topic);
        dest.writeString(subTopic);
        dest.writeString(modeOfDiscussion);
        dest.writeString(outcomeAndComments);
        dest.writeList(discussionModes);
        dest.writeList(discussionOutcomes);
    }

    public static final Parcelable.Creator<InterviewSummary> CREATOR = new Parcelable.Creator<InterviewSummary>() {
        public InterviewSummary createFromParcel(Parcel in) {
            return new InterviewSummary(in);
        }

        public InterviewSummary[] newArray(int size) {
            return new InterviewSummary[size];
        }
    };
}
