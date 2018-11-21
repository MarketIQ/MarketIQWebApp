package com.app.server.models;
public class BusinessCompany {



    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getCategory() {
        return category;
    }
    public String getEmailAddress() {
        return emailAddress;
    }

    String id=null;
    String name;
    String emailAddress;
    String address;
    String category;
    String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public BusinessCompany(String name, String emailAddress, String address, String category, String phoneNumber) {

        this.name = name;
        this.emailAddress = emailAddress;
        this.address = address;
        this.category = category;
        this.phoneNumber = phoneNumber;
    }
    public void setId(String id) {
        this.id = id;
    }
}