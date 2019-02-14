package com.synechrone.interviewfeedback.ws.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class DiscussionDetails {
    @SerializedName("id")
    private long id;
    @SerializedName("subTopicId")
    private int subTopicId;
    @SerializedName("interviewId")
    private long interviewId;
    @SerializedName("discussionModeIds")
    private List<Integer> discussionModeIds;
    @SerializedName("outcomesIds")
    private List<Integer> outcomesIds;
    @SerializedName("comment")
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSubTopicId() {
        return subTopicId;
    }

    public void setSubTopicId(int subTopicId) {
        this.subTopicId = subTopicId;
    }

    public long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(long interviewId) {
        this.interviewId = interviewId;
    }

    public List<Integer> getDiscussionModeIds() {
        return discussionModeIds;
    }

    public void setDiscussionModeIds(List<Integer> discussionModeIds) {
        this.discussionModeIds = discussionModeIds;
    }

    public List<Integer> getOutcomesIds() {
        return outcomesIds;
    }

    public void setOutcomesIds(List<Integer> outcomesIds) {
        this.outcomesIds = outcomesIds;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
