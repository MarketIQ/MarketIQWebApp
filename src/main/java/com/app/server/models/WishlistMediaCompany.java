
package com.app.server.models;

public class WishlistMediaCompany {

    public String getMediaCompanyId() {
        return mediaCompanyId;
    }

    public String getWishListEntry() {
        return wishListEntry;
    }

    public String getCreationDate() {
        return creationDate;
    }

    String wishListId;
    String mediaCompanyId;
    String wishListEntry;
    String creationDate;


    public WishlistMediaCompany(String mediaCompanyId, String wishListEntry, String creationDate) {
        this.mediaCompanyId=mediaCompanyId;
        this.wishListEntry=wishListEntry;
        this.creationDate=creationDate;

    }
    public void setMediaCompanyWishListId(String id) {
        this.wishListId= id;
    }
}
