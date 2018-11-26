package com.app.server.models;

public class Transaction {

    String transactionId;
    String timestamp;
    String amount;
    String subscriptionPeriod;

    public Transaction(String timestamp, String amount, String subscriptionPeriod) {

        this.timestamp = timestamp;
        this.amount = amount;
        this.subscriptionPeriod = subscriptionPeriod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setSubscriptionPeriod(String subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }
}