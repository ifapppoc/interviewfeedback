package com.synechrone.interviewfeedback.domain;

import java.io.Serializable;
import java.util.Date;

public class CandidateDetails implements Serializable
{
    private static final long serialVersionUID = -4525373576878423358L;
    private String interviewerName;
    private String candidateName;
    private String candidateEmail;
    private Date interviewDate;
    private String technologyTested;

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(String interviewerName) {
        this.interviewerName = interviewerName;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getTechnologyTested() {
        return technologyTested;
    }

    public void setTechnologyTested(String technologyTested) {
        this.technologyTested = technologyTested;
    }
}
