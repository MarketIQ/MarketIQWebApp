package com.app.server.models;


import com.app.server.http.utils.APPCrypt;

public class Session {

    String token = null;
    String userId = null;
    String name = null;

//    public Session(BusinessCompany user) throws Exception{
//        this.userId = user.id;
//        this.token = APPCrypt.encrypt(user.emailAddress);
//        this.name = user.name;
//    }
//
//    public Session(MediaCompany user) throws Exception{
//        this.userId = user.id;
//        this.token = APPCrypt.encrypt(user.emailAddress);
//        this.name = user.name;
//    }

    public Session(Company company) throws Exception {
        this.userId = company.getId();
        this.name = company.getName();
        this.token = APPCrypt.encrypt(company.getEmailAddress());
    }
}
