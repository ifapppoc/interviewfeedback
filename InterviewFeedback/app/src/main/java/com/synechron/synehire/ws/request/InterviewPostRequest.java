package com.synechron.synehire.ws.request;

import com.google.gson.annotations.SerializedName;
import com.synechron.synehire.ws.response.Candidate;

public class InterviewPostRequest {
    @SerializedName("techId")
    private int techId;
    @SerializedName("recruiterId")
    private String recruiterId;
    @SerializedName("panelId")
    private String panelId;
    @SerializedName("levelId")
    private int levelId;
    @SerializedName("candidate")
    private Candidate candidate;
    @SerializedName("interviewModeId")
    private int interviewModeId;

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
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

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public int getInterviewModeId() {
        return interviewModeId;
    }

    public void setInterviewModeId(int interviewModeId) {
        this.interviewModeId = interviewModeId;
    }
}
