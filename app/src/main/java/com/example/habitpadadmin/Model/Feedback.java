package com.example.habitpadadmin.Model;

public class Feedback {

    private String feedbackID, fEmail, fDetails, fDate;

    public Feedback(String feedbackID, String fEmail , String fDetails, String fDate){
        this.feedbackID = feedbackID;
        this.fEmail = fEmail;
        this.fDetails = fDetails;
        this.fDate = fDate;

    }

    public String getFeedbackID() {
        return feedbackID;
    }

    public String getfEmail() {
        return fEmail;
    }

    public String getfDetails() {
        return fDetails;
    }

    public String getfDate() {
        return fDate;
    }


}
