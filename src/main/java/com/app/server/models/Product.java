package com.app.server.models;
public class Product {
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getStartPrice() {
        return startPrice;
    }
    public String getEndPrice() {
        return endPrice;
    }
    String id=null;
    String name;
    String startPrice;
    String endPrice;
    String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public Product(String name, String startPrice, String endPrice, String emailAddress) {
        this.name = name;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.emailAddress = emailAddress;
    }
    public void setId(String id) {
        this.id = id;
    }
}

