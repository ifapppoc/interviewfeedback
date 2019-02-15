package com.synechron.synehire.ws.request;

import com.google.gson.annotations.SerializedName;
import com.synechron.synehire.ws.response.EmailId;

import java.util.List;

public class EmailRequest {
    @SerializedName("candidateEmailIds")
    private List<EmailId> candidateEmailIds;
    @SerializedName("senderEmailId")
    private String senderEmailId;
    @SerializedName("recipientEmailIds")
    private List<EmailId> recipientEmailIds;

    public List<EmailId> getCandidateEmailIds() {
        return candidateEmailIds;
    }

    public void setCandidateEmailIds(List<EmailId> candidateEmailIds) {
        this.candidateEmailIds = candidateEmailIds;
    }

    public String getSenderEmailId() {
        return senderEmailId;
    }

    public void setSenderEmailId(String senderEmailId) {
        this.senderEmailId = senderEmailId;
    }

    public List<EmailId> getRecipientEmailIds() {
        return recipientEmailIds;
    }

    public void setRecipientEmailIds(List<EmailId> recipientEmailIds) {
        this.recipientEmailIds = recipientEmailIds;
    }
}
