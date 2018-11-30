
package com.app.server.models;

import com.app.server.models.PaymentDetails;

public class Transaction {

    private String id = null;
    private  String emailAddress;
    private PaymentDetails paymentDetails;
    private String transactionState;
    private String transactionTime;

    public Transaction(String emailAddress, PaymentDetails paymentDetails, String transactionState, String transactionTime) {
        this.emailAddress = emailAddress;
        this.paymentDetails = paymentDetails;
        this.transactionState = transactionState;
        this.transactionTime = transactionTime;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getTransactionState() {
        return transactionState;
    }

    public void setTransactionState(String transactionState) {
        this.transactionState = transactionState;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

}