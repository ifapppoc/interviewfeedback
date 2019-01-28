package com.synechrone.interviewfeedback.ws.request;

public class Candidate {

    private String candidateEmailID;
    private String firstName;
    private String lastName;
    private int techId;
    private String primaryContactNumber;
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

    @Override
    public String toString() {
        return "Candidate{" +
                "candidateEmailID='" + candidateEmailID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", techId=" + techId +
                ", primaryContactNumber='" + primaryContactNumber + '\'' +
                ", alternateContactNumber='" + alternateContactNumber + '\'' +
                '}';
    }
}
