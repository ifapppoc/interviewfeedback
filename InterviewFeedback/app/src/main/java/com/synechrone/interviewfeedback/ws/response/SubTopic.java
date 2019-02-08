package com.synechrone.interviewfeedback.ws.response;

import com.google.gson.annotations.SerializedName;

public class SubTopic {
    @SerializedName("subtopicId")
    private int subtopicId;
    @SerializedName("topicId")
    private int topicId;
    @SerializedName("subTopicName")
    private String subTopicName;

    public int getSubtopicId() {
        return subtopicId;
    }

    public void setSubtopicId(int subtopicId) {
        this.subtopicId = subtopicId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getSubTopicName() {
        return subTopicName;
    }

    public void setSubTopicName(String subTopicName) {
        this.subTopicName = subTopicName;
    }

    @Override
    public String toString() {
        return subTopicName;
    }
}
