package com.synechrone.interviewfeedback.ws.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InterviewSummary {
    @SerializedName("topic")
    private String topic;
    @SerializedName("subTopic")
    private String subTopic;
    @SerializedName("discussionDetailId")
    private long discussionDetailId;
    @SerializedName("discussionModes")
    private List<DiscussionMode> discussionModes;
    @SerializedName("discussionOutcomes")
    private List<DiscussionOutcome> discussionOutcomes;
    @SerializedName("comment")
    private String comment;

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

    public long getDiscussionDetailId() {
        return discussionDetailId;
    }

    public void setDiscussionDetailId(long discussionDetailId) {
        this.discussionDetailId = discussionDetailId;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
