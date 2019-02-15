package com.synechron.synehire.ws.response;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class DiscussionOutcome {
    @SerializedName("id")
    private int id;
    @SerializedName("outcome")
    private String outcome;

    public DiscussionOutcome(int id, String outcome) {
        this.id = id;
        this.outcome = outcome;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscussionOutcome that = (DiscussionOutcome) o;
        return id == that.id &&
                Objects.equals(outcome, that.outcome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, outcome);
    }
}
