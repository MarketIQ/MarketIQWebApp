package com.app.server.models;

import org.bson.types.ObjectId;

public class ThirdPartyPayment {

    String transactionId;//out
    String userReferenceId;//in
    String name;//in
    Double paymentAmount;//in
    String paymentStatus;//out
    String paymentTime;//out
    String emailAddress;//in

    public ThirdPartyPayment(String userReferenceId, String name, Double paymentAmount, String paymentTime, String emailAddress) {
        setTransactionId(emailAddress);
        this.userReferenceId = userReferenceId;
        this.name = name;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = "PENDING";
        this.paymentTime = paymentTime;
        this.emailAddress = emailAddress;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String emailAddress) {


        this.transactionId = new ObjectId(emailAddress).toString();
    }

    public String getUserReferenceId() {
        return userReferenceId;
    }

    public void setUserReferenceId(String userReferenceId) {
        this.userReferenceId = userReferenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}