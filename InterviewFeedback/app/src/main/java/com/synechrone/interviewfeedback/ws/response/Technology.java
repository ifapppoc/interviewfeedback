package com.synechrone.interviewfeedback.ws.response;

import com.google.gson.annotations.SerializedName;

public class Technology {
    @SerializedName("techId")
    private int techId;
    @SerializedName("technologyName")
    private String technologyName;
    @SerializedName("description")
    private String description;

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return technologyName;
    }
}
