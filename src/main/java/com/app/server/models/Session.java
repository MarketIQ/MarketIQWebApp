package com.app.server.models;


import com.app.server.http.utils.APPCrypt;

public class Session {

    String token = null;
    String userId = null;
    String Name = null;

    public Session(Users user) throws Exception{
        this.userId = user.userId;
        this.token = APPCrypt.encrypt(user.userId);
        this.Name = user.userName;
    }
}
