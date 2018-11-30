package com.app.server.models;

public class MediaCompany {

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    //String id;
    String name;
    String category;
    String subCategory;
    String emailAddress;
    String address;
    String phoneNumber;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MediaCompany(
                        String emailAddress) {

        this.emailAddress=emailAddress;

    }

//    public void setId(String id) {
//        this.id = id;
//    }
}
