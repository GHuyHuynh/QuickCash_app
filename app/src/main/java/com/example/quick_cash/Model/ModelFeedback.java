package com.example.quick_cash.Model;

/**
 * Model object for the Feedback it includes the user who typed the feedback
 * and Feedback itself
 */
public class ModelFeedback {
    String username;
    String comment;

    public ModelFeedback(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
