package com.app.server.models;

import java.util.List;

public class PaymentDetails {

    String emailAddress;

    List<PaymentMethod> paymentMethodList;

    public PaymentDetails() {
    }

    public PaymentDetails(String emailAddress, List<PaymentMethod> paymentMethodList) {
        this.emailAddress = emailAddress;
        this.paymentMethodList = paymentMethodList;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<PaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(List<PaymentMethod> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }


}