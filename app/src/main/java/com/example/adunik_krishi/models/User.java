package com.example.adunik_krishi.models;

public class User {
    private String uID,name,phone,city,password;

    public User() {
    }

    public User(String uID, String name, String phone, String city, String password) {
        this.uID = uID;
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.password = password;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
