package com.synechrone.interviewfeedback.ws.response;

public class Technology {
    private int id;
    private String technologyName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    @Override
    public String toString() {
        return "Technology{" +
                "id=" + id +
                ", technologyName='" + technologyName + '\'' +
                '}';
    }
}
