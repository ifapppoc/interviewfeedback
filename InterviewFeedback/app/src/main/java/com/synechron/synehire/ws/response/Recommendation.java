package com.synechron.synehire.ws.response;

import com.google.gson.annotations.SerializedName;

public class Recommendation {
    @SerializedName("id")
    private int id;
    @SerializedName("levelId")
    private int levelId;
    @SerializedName("recommendation")
    private String recommendation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
