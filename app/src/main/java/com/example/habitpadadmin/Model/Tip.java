package com.example.habitpadadmin.Model;

public class Tip {
    private String tipID,tipDetails,tipPhoto, tipDateTime;


    public Tip(String tipID, String tipDetails , String tipPhoto , String tipDateTime ){
        this.tipID = tipID;
        this.tipDetails = tipDetails;
        this.tipPhoto = tipPhoto;
        this.tipDateTime = tipDateTime;

    }

    public String getTipID() {
        return tipID;
    }

    public String getTipDetails() {
        return tipDetails;
    }

    public String getTipPhoto() {
        return tipPhoto;
    }

    public String getTipDateTime() {
        return tipDateTime;
    }
}
