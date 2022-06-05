package com.example.habitpadadmin.Model;

public class Voucher {

    private String voucherID, image, point, title, desc;

    public Voucher(String voucherID, String image , String point, String title, String desc){
        this.voucherID = voucherID;
        this.image = image;
        this.point = point;
        this.title = title;
        this.desc = desc;
    }

    public String getVoucherID() {
        return voucherID;
    }

    public String getImage() {
        return image;
    }

    public String getPoint() {
        return point;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

}
