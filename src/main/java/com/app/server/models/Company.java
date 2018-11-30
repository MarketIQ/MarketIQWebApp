package com.app.server.models;

import java.util.List;

public class Company {

    String id;
    String name;
    String type;
    String emailAddress;
    List<Transactionss> transactionssList;

    public Company(String id, String name, String type, String emailAddress, List<Transactionss> transactionssList) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.emailAddress = emailAddress;
        this.transactionssList = transactionssList;
    }

    public List<Transactionss> getTransactionssList() {
        return transactionssList;
    }

    public void setTransactionssList(List<Transactionss> transactionssList) {
        this.transactionssList = transactionssList;
    }

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
