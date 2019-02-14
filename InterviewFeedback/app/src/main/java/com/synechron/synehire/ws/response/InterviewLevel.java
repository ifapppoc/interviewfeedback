package com.synechrone.synehire.ws.response;

import com.google.gson.annotations.SerializedName;

public class InterviewLevel {
    @SerializedName("levelId")
    private int levelId;
    @SerializedName("levelName")
    private String levelName;
    @SerializedName("desc")
    private String desc;

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return levelName;
    }
}
