package com.example.habitpadadmin.Model;

public class Challenge {

    private String challengeID, challengeImage, challengeTitle, challengeDesc, challengeStartDate, challengeEndDate, challengeStep;

    public Challenge(String challengeID, String challengeImage , String challengeTitle,String challengeDesc,String challengeStartDate,String challengeEndDate,String challengeStep){
        this.challengeID = challengeID;
        this.challengeImage = challengeImage;
        this.challengeTitle = challengeTitle;
        this.challengeDesc = challengeDesc;
        this.challengeStartDate = challengeStartDate;
        this.challengeEndDate = challengeEndDate;
        this.challengeStep = challengeStep;
    }

    public String getChallengeID() {
        return challengeID;
    }

    public String getChallengeImage() {
        return challengeImage;
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public String getChallengeDesc() {
        return challengeDesc;
    }

    public String getChallengeStartDate() {
        return challengeStartDate;
    }

    public String getChallengeEndDate() {
        return challengeEndDate;
    }

    public String getChallengeStep() {
        return challengeStep;
    }

}
