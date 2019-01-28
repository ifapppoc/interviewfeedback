package com.synechrone.interviewfeedback.ws.response;

public class Topic {

    private int topicId;
    private int techId;
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
        return "Topic{" +
                "topicId=" + topicId +
                ", techId=" + techId +
                ", topicName='" + topicName + '\'' +
                '}';
    }
}
