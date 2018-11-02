
package com.app.server.models;

public class WishList {

    public String getBusinessId() {
        return businessId;
    }

    public String getWishListEntry() {
        return wishListEntry;
    }

    public String getCreationDate() {
        return creationDate;
    }

    String wishListId;
    String businessId;
    String wishListEntry;
    String creationDate;


    public WishList(String businessId, String wishListEntry, String creationDate) {
        this.businessId=businessId;
        this.wishListEntry=wishListEntry;
        this.creationDate=creationDate;

    }
    public void setWishListId(String id) {
        this.wishListId= id;
    }
}
