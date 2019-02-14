package com.synechrone.synehire.ws.request;

import com.google.gson.annotations.SerializedName;

public class RecommendationDetails {
    @SerializedName("recommendationId")
    private int recommendationId;
    @SerializedName("recommendationComment")
    private String recommendationComment;

    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public String getRecommendationComment() {
        return recommendationComment;
    }

    public void setRecommendationComment(String recommendationComment) {
        this.recommendationComment = recommendationComment;
    }
}
