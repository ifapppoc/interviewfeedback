package com.synechron.synehire.ws.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DiscussionDetailsSummary {
    @SerializedName("discussions")
    private List<DiscussionDetails> discussions;

    public List<DiscussionDetails> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<DiscussionDetails> discussions) {
        this.discussions = discussions;
    }
}
