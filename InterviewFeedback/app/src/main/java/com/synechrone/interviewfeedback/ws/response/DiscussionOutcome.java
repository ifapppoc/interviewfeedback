package com.synechrone.interviewfeedback.ws.response;

import com.google.gson.annotations.SerializedName;

public class DiscussionOutcome {
    @SerializedName("id")
    private int id;
    @SerializedName("outcome")
    private String outcome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return outcome;
    }
}
