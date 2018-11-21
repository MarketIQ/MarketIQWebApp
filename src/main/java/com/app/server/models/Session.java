package com.app.server.models;


import com.app.server.http.utils.APPCrypt;

public class Session {

    String token = null;
    String userId = null;
    String Name = null;

    public Session(BusinessCompany user) throws Exception{
        this.userId = user.id;
        this.token = APPCrypt.encrypt(user.id);
        this.Name = user.name;
    }

    public Session(MediaCompany user, String role) throws Exception{
        this.userId = user.id;
        this.token = APPCrypt.encrypt(user.id);
        this.Name = user.name;
    }
}
