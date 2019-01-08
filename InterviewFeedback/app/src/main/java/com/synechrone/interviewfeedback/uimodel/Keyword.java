package com.synechrone.interviewfeedback.uimodel;

public class Keyword {
    private boolean isSelected;
    private String keyword;

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getKeyword() {
        return keyword;
    }
}
