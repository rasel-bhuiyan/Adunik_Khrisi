package com.example.adunik_krishi.models;

public class Product {
    private String pName,pDetails,pQuantity,pAmount,pPhone,pImage;

    public Product() {
    }

    public Product(String pName, String pDetails, String pQuantity, String pAmount, String pPhone, String pImage) {
        this.pName = pName;
        this.pDetails = pDetails;
        this.pQuantity = pQuantity;
        this.pAmount = pAmount;
        this.pPhone = pPhone;
        this.pImage = pImage;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpDetails() {
        return pDetails;
    }

    public void setpDetails(String pDetails) {
        this.pDetails = pDetails;
    }

    public String getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }

    public String getpAmount() {
        return pAmount;
    }

    public void setpAmount(String pAmount) {
        this.pAmount = pAmount;
    }

    public String getpPhone() {
        return pPhone;
    }

    public void setpPhone(String pPhone) {
        this.pPhone = pPhone;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}
