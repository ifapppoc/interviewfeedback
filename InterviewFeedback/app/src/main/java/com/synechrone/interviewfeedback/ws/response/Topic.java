package com.synechrone.interviewfeedback.ws.response;

import com.google.gson.annotations.SerializedName;

public class Topic {
    @SerializedName("topicId")
    private int topicId;
    @SerializedName("techId")
    private int techId;
    @SerializedName("topicName")
    private String topicName;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        return topicName;
    }
}
