package com.synechrone.synehire.ws.response;

import com.google.gson.annotations.SerializedName;

public class Candidate {
    @SerializedName("candidateEmailID")
    private String candidateEmailID;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("techId")
    private int techId;
    @SerializedName("primaryContactNumber")
    private String primaryContactNumber;
    @SerializedName("alternateContactNumber")
    private String alternateContactNumber;

    public String getCandidateEmailID() {
        return candidateEmailID;
    }

    public void setCandidateEmailID(String candidateEmailID) {
        this.candidateEmailID = candidateEmailID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getTechId() {
        return techId;
    }

    public void setTechId(int techId) {
        this.techId = techId;
    }

    public String getPrimaryContactNumber() {
        return primaryContactNumber;
    }

    public void setPrimaryContactNumber(String primaryContactNumber) {
        this.primaryContactNumber = primaryContactNumber;
    }

    public String getAlternateContactNumber() {
        return alternateContactNumber;
    }

    public void setAlternateContactNumber(String alternateContactNumber) {
        this.alternateContactNumber = alternateContactNumber;
    }
}
