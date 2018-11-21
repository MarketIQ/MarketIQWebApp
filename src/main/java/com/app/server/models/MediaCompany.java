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

    String id;
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

    public MediaCompany(String name, String category, String subCategory,
                        String emailAddress, String address, String phoneNumber) {
        this.name = name;
        this.category=category;
        this.subCategory=subCategory;
        this.emailAddress=emailAddress;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public void setId(String id) {
        this.id = id;
    }
}
