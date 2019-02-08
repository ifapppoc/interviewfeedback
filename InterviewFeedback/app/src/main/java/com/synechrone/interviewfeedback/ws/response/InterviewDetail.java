package com.synechrone.interviewfeedback.ws.response;

import com.google.gson.annotations.SerializedName;

public class InterviewDetail {
    @SerializedName("techId")
    private int techId;
    @SerializedName("candidateEmailId")
    private String candidateEmailId;
    @SerializedName("panelId")
    private String panelId;
    @SerializedName("levelId")
    private int levelId;
    @SerializedName("interviewModeId")
    private int interviewModeId;

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getCandidateEmailId() {
        return candidateEmailId;
    }

    public void setCandidateEmailId(String candidateEmailId) {
        this.candidateEmailId = candidateEmailId;
    }

    public String getPanelId() {
        return panelId;
    }

    public void setPanelId(String panelId) {
        this.panelId = panelId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getInterviewModeId() {
        return interviewModeId;
    }

    public void setInterviewModeId(int interviewModeId) {
        this.interviewModeId = interviewModeId;
    }
}
