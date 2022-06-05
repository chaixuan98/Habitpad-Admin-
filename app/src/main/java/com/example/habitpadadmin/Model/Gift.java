package com.example.habitpadadmin.Model;

public class Gift {

    private String giftID, gimage, gpoint, gtitle, gdesc;

    public Gift(String giftID, String gimage , String gpoint, String gtitle, String gdesc){
        this.giftID = giftID;
        this.gimage = gimage;
        this.gpoint = gpoint;
        this.gtitle = gtitle;
        this.gdesc = gdesc;
    }

    public String getGiftID() {
        return giftID;
    }

    public String getGimage() {
        return gimage;
    }

    public String getGpoint() {
        return gpoint;
    }

    public String getGtitle() {
        return gtitle;
    }

    public String getGdesc() {
        return gdesc;
    }

}
