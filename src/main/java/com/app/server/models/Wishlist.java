
package com.app.server.models;

import java.util.List;

public class Wishlist {

    String emailAddress;
    List<String> wishListCompanies;

    public Wishlist(String emailAddress, List<String> wishListCompanies) {
        this.emailAddress = emailAddress;
        this.wishListCompanies = wishListCompanies;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<String> getWishListCompanies() {
        return wishListCompanies;
    }

    public void setWishListCompanies(List<String> wishListCompanies) {
        this.wishListCompanies = wishListCompanies;
    }
}