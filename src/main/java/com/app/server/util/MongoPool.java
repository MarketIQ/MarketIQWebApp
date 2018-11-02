package com.app.server.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoPool {

    private static MongoPool mp;
    private static MongoDatabase db;


    private MongoPool() {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        //build the connection options
        builder.maxConnectionIdleTime(60000);//set the max wait time in (ms)
        builder.socketKeepAlive(true);
        builder.connectTimeout(30000);
        MongoClientOptions opts = builder.build();

        MongoClient mc = new MongoClient(new ServerAddress());
        db =mc.getDatabase("marketIQ");
        Logger.getLogger("org.mongodb.business").setLevel(Level.WARNING);
    }

    public static MongoPool getInstance(){
        if(mp == null){
            mp = new MongoPool();
        }
        return mp;
    }
    public MongoCollection<Document> getCollection(String collectionName){
        return db.getCollection(collectionName);
    }
}
