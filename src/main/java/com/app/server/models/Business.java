package com.app.server.models;
public class Business {
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getType() {
        return type;
    }
    String id=null;
    String name;
    String address;
    String type;
    public Business(String name, String address, String type) {
        this.name = name;
        this.address = address;
        this.type = type;
    }
    public void setId(String id) {
        this.id = id;
    }
}