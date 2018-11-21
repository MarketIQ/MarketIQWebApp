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
    String businessId;

    public String getBusinessId() {
        return businessId;
    }

    public Product(String name, String startPrice, String endPrice, String businessId) {
        this.name = name;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.businessId = businessId;
    }
    public void setId(String id) {
        this.id = id;
    }
}

