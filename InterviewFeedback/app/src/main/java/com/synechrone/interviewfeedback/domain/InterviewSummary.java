package com.synechrone.interviewfeedback.domain;

import java.io.Serializable;

public class InterviewSummary implements Serializable {

    private static final long serialVersionUID = -2122987603082478268L;
    private String mainTopic;
    private String subTopic;
    private String modeOfDiscussion;
    private String outcomeAndComments;

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
}
