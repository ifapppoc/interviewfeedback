package com.synechrone.interviewfeedback.ws.response;

import com.google.gson.annotations.SerializedName;

public class InterviewMode {
    @SerializedName("id")
    private int id;
    @SerializedName("modeName")
    private String modeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    @Override
    public String toString() {
        return modeName;
    }
}
