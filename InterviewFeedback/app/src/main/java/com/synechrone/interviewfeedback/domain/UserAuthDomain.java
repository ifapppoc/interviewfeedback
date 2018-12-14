package com.synechrone.interviewfeedback.domain;

import java.io.Serializable;

public class UserAuthDomain implements Serializable {

    private boolean authenticated = false;

    private String errorMessage = "";


    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
