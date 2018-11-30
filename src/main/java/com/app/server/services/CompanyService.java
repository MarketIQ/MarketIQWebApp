package com.app.server.services;

import com.app.server.http.exceptions.APPBadRequestException;
import com.app.server.http.exceptions.APPConflictException;
import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.http.utils.APPCrypt;
import com.app.server.models.Session;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Services run as singletons
 */
public class CompanyService {
    private static CompanyService self;
    private ObjectWriter ow;
    private MongoCollection<Document> companyCollection = null;
    private SessionService sessionService;
    private BusinessCompanyService businessCompanyService;
    private MediaCompanyService mediaCompanyService;


    private CompanyService() {
        this.companyCollection = MongoPool.getInstance().getCollection("companies");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        this.sessionService = new SessionService();
        this.businessCompanyService = BusinessCompanyService.getInstance();
        this.mediaCompanyService = MediaCompanyService.getInstance();
    }

    public static CompanyService getInstance() {
        if (self == null)
            self = new CompanyService();
        return self;
    }

    public Object register(Object request) {

         Map<String, Object> map = new HashMap<>();
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            String emailId = json.getString("emailAddress");

            BasicDBObject query = new BasicDBObject();
            query.put("emailAddress", emailId);

            if (companyCollection.find(query).first() != null) {
                throw new APPConflictException(409, "user with same email already exists");
            }

            map.put("emailAddress", json.getString("emailAddress"));
//            map.put("name", json.getString("name"));
            map.put("type", json.getString("type"));
            map.put("password", APPCrypt.encrypt(json.getString("password")));

            Document doc = new Document(map);
            companyCollection.insertOne(doc);
            System.out.printf("inserted");
            businessCompanyService.create(request);
            map.remove("password");

        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }


    public Object login(Object request) {

        JSONObject json = null;

        try {
            json = new JSONObject(ow.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new APPBadRequestException(4011, "Invalid Json");
        }
        String emailAddress = json.getString("emailAddress");
        String password = json.getString("password");

        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", emailAddress);
        query.put("password", APPCrypt.encrypt(password));

        if (companyCollection.find(query).first() == null) {
            throw new APPUnauthorizedException(409, "invalid username/password");
        }

        Session session = sessionService.create(request);



        return session;
    }


} // end of main()