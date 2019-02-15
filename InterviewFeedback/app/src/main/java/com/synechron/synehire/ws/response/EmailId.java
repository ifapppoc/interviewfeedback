package com.synechron.synehire.ws.response;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class EmailId {
    @SerializedName("emailId")
    private String emailId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return emailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailId emailId1 = (EmailId) o;
        return Objects.equals(emailId, emailId1.emailId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailId);
    }
}
