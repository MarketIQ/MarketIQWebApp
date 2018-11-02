package com.app.server.models;

public class Users {

    public String getUserName() {
        return userName;
    }

    public String getUserBusinessId() {
        return userBusinessId;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    String userId;
    String userName;
    String userBusinessId;
    String userRole;
    String userEmailId;


    public Users(String userName, String userBusinessid, String userRole, String userEmailId) {
        this.userName=userName;
        this.userBusinessId=userBusinessid;
        this.userRole=userRole;
        this.userEmailId=userEmailId;

    }
    public void setUserId(String id) {
        this.userId= id;
    }
}
