package com.synechrone.interviewfeedback.domain;

public enum EmployeeRole {
    RECRUITER("RECRUITER"), PANELIST("PANELIST");

    private String role;

    EmployeeRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
