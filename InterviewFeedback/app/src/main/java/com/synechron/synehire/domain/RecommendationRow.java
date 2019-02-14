package com.synechrone.synehire.domain;

public class RecommendationRow {
    private boolean isCollapsed = true;
    private int recommendationId;
    private String recommendation;
    private String comment;

    public RecommendationRow(int recommendationId, String recommendation) {
        this.recommendationId = recommendationId;
        this.recommendation = recommendation;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
    }

    public int getRecommendationId() {
        return recommendationId;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
