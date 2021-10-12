package com.example.adunik_krishi.models;

public class Question {
    private String qID,qTitle,qDetails,qPhone,qImage,qDate;

    public Question(String qID, String qTitle, String qDetails, String qPhone, String qImage, String qDate) {
        this.qID = qID;
        this.qTitle = qTitle;
        this.qDetails = qDetails;
        this.qPhone = qPhone;
        this.qImage = qImage;
        this.qDate = qDate;
    }

    public Question() {
    }

    public String getqID() {
        return qID;
    }

    public void setqID(String qID) {
        this.qID = qID;
    }

    public String getqTitle() {
        return qTitle;
    }

    public void setqTitle(String qTitle) {
        this.qTitle = qTitle;
    }

    public String getqDetails() {
        return qDetails;
    }

    public void setqDetails(String qDetails) {
        this.qDetails = qDetails;
    }

    public String getqPhone() {
        return qPhone;
    }

    public void setqPhone(String qPhone) {
        this.qPhone = qPhone;
    }

    public String getqImage() {
        return qImage;
    }

    public void setqImage(String qImage) {
        this.qImage = qImage;
    }

    public String getqDate() {
        return qDate;
    }

    public void setqDate(String qDate) {
        this.qDate = qDate;
    }
}
