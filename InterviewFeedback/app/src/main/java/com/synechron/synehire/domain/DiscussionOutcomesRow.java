package com.synechron.synehire.domain;

public class DiscussionOutcomesRow {

    private boolean isSelected;
    private int id;
    private String outcome;

    public DiscussionOutcomesRow(int id , String outcome, boolean isSelected) {
        this.isSelected = isSelected;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return outcome;
    }
}
