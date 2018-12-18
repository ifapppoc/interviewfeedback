package com.synechrone.interviewfeedback.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class InterviewSummary implements Parcelable {

    private String mainTopic;
    private String subTopic;
    private String modeOfDiscussion;
    private String outcomeAndComments;

    public InterviewSummary() {

    }

    public InterviewSummary(Parcel in) {
        mainTopic = in.readString();
        subTopic = in.readString();
        modeOfDiscussion = in.readString();
        outcomeAndComments = in.readString();
    }

    public String getMainTopic() {
        return mainTopic;
    }

    public void setMainTopic(String mainTopic) {
        this.mainTopic = mainTopic;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mainTopic);
        dest.writeString(subTopic);
        dest.writeString(modeOfDiscussion);
        dest.writeString(outcomeAndComments);
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
