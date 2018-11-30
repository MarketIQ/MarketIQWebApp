package com.app.server.models;

import org.bson.types.ObjectId;

import java.util.List;

public class PaymentDetails {

    String payment_id;
    String emailAddress;
    String cardNumber;
    String cvvNumber;
    String expiryDate;
    String cardName;
    Double amount;
    String subscriptionPeriod;

    public PaymentDetails() {
    }

    public PaymentDetails(String emailAddress, String cardNumber, String cvvNumber, String expiryDate, String cardName, Double amount, String subscriptionPeriod) {

        this.emailAddress = emailAddress;
        this.cardNumber = cardNumber;
        this.cvvNumber = cvvNumber;
        this.expiryDate = expiryDate;
        this.cardName = cardName;
        this.amount = amount;
        this.subscriptionPeriod = subscriptionPeriod;
        setPayment_id(emailAddress);
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvvNumber() {
        return cvvNumber;
    }

    public void setCvvNumber(String cvvNumber) {
        this.cvvNumber = cvvNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String emailAddress) {
        this.payment_id = new ObjectId(emailAddress).toString();
    }

    public String getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setSubscriptionPeriod(String subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }
}