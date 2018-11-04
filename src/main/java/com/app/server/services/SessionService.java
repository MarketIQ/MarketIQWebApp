package com.app.server.services;

import com.app.server.http.exceptions.APPBadRequestException;
import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPCrypt;
import com.app.server.models.Users;
import com.app.server.models.Session;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Services run as singletons
 */

public class SessionService {

    private static SessionService self;
    private ObjectWriter ow;
    private MongoCollection<Document> usersCollection = null;

    private SessionService() {
        this.usersCollection = MongoPool.getInstance().getCollection("users");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static SessionService getInstance(){
        if (self == null)
            self = new SessionService();
        return self;
    }

    public Session create(Object request) {

        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
            if (!json.has("emailAddress"))
                throw new APPBadRequestException(55, "missing emailAddress");
            if (!json.has("password"))
                throw new APPBadRequestException(55, "missing password");
            BasicDBObject query = new BasicDBObject();

            query.put("emailAddress", json.getString("emailAddress"));
            query.put("password", APPCrypt.encrypt(json.getString("password")));

            Document item = usersCollection.find(query).first();
            if (item == null) {
                throw new APPNotFoundException(0, "No user found matching credentials");
            }

            Users user = convertDocumentToUser(item);

            user.setUserId(item.getObjectId("_id").toString());
            return new Session(user);
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
        catch (APPBadRequestException e) {
            throw e;
        }
        catch (APPNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new APPInternalServerException(0, e.getMessage());
        }
    }


    private Users convertDocumentToUser(Document item) {
        Users user = new Users(
                item.getString("userName"),
                item.getString("userBusinessId"),
                item.getString("userRole"),
                item.getString("userEmailId")
        );
        user.setUserId(item.getObjectId("_id").toString());
        return user;
    }



} // end of main()
