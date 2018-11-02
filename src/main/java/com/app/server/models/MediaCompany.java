package com.app.server.models;

public class MediaCompany {

    public String getMediaCompanyName() {
        return mediaCompanyName;
    }

    public String getMediaCompanyEmailId() {
        return mediaCompanyEmailId;
    }

    public String getMediaCompanyPhone() {
        return mediaCompanyPhone;
    }

    String mediaCompanyId;
    String mediaCompanyName;
    String mediaCompanyEmailId;
    String mediaCompanyPhone;


    public MediaCompany(String mediaCompanyName, String mediaCompanyEmailId, String mediaCompanyPhone) {
        this.mediaCompanyName=mediaCompanyName;
        this.mediaCompanyEmailId=mediaCompanyEmailId;
        this.mediaCompanyPhone=mediaCompanyPhone;
    }

    public void setMediaCompanyId(String id) {
        this.mediaCompanyId= id;
    }
}
