package com.synechrone.interviewfeedback.ws.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class InterviewRecommendation {
    @SerializedName("interviewId")
    private long interviewId;
    @SerializedName("recommendations")
    private List<RecommendationDetails> recommendations;

    public long getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(long interviewId) {
        this.interviewId = interviewId;
    }

    public List<RecommendationDetails> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationDetails> recommendations) {
        this.recommendations = recommendations;
    }
}
