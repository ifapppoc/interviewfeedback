package com.synechrone.interviewfeedback.ws.response;

public class SubTopic {

    private int subtopicId;
    private int topicId;
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
        return "SubTopic{" +
                "subtopicId=" + subtopicId +
                ", topicId=" + topicId +
                ", subTopicName='" + subTopicName + '\'' +
                '}';
    }
}
