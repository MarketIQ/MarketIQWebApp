package com.app.server.models;
public class BusinessCompany {



//    public String getId() {
//        return id;
//    }
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

   // String id=null;
    String name;
    String emailAddress;
    String address;
    String category;
    String phoneNumber;


    public void setName(String name) {
        this.name = name;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BusinessCompany(String emailAddress) {
        this.emailAddress = emailAddress;
    }
//    public void setId(String id) {
//        this.id = id;
//    }
}