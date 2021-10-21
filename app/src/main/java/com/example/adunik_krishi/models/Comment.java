package com.example.adunik_krishi.models;

public class Comment {

    private String cID,comment,phone;

    public Comment() {
    }

    public Comment(String cID, String comment, String phone) {
        this.cID = cID;
        this.comment = comment;
        this.phone = phone;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
