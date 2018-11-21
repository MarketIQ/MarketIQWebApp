package com.app.server.models;

public class Company {
    

    String id;
    String name;
    String type;
    String emailAddress;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id= id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
