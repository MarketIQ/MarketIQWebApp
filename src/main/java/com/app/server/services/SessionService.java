package com.app.server.services;
import com.app.server.models.MediaCompany;
import com.app.server.models.Session;
import com.app.server.http.exceptions.APPBadRequestException;
import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPCrypt;
import com.app.server.models.BusinessCompany;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.json.JSONObject;

/**
 * Services run as singletons
 */

public class SessionService {

    private static SessionService self;
    private ObjectWriter ow;
    private MongoCollection<Document> businessusersCollection = null;
    private MongoCollection<Document> mediaCompanyCollection = null;


    private SessionService() {
        this.businessusersCollection = MongoPool.getInstance().getCollection("businessusers");
        this.mediaCompanyCollection = MongoPool.getInstance().getCollection("mediacompany");

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


            Document item;
            if (!json.has("emailAddress"))
                throw new APPBadRequestException(55, "missing emailAddress");
            if (!json.has("password"))
                throw new APPBadRequestException(55, "missing password");
            BasicDBObject query = new BasicDBObject();

            query.put("emailAddress", json.getString("emailAddress"));
            query.put("password", APPCrypt.encrypt(json.getString("password")));

            if(json.getString("usertype").equals("business")){
                item = businessusersCollection.find(query).first();
                if (item == null) {
                    throw new APPNotFoundException(0, "No user found matching credentials");
                }
                BusinessCompany businessuser = convertDocumentToBusinessUser(item);
                businessuser.setId(item.getObjectId("_id").toString());
                return new Session(businessuser);
            }
            else{
                item = mediaCompanyCollection.find(query).first();
                if (item == null) {
                    throw new APPNotFoundException(0, "No user found matching credentials");
                }
                MediaCompany mediaCompanyuser = convertDocumentToMediaCompanyUser(item);
                mediaCompanyuser.setId(item.getObjectId("_id").toString());
                return new Session(mediaCompanyuser,"mediaCompany");
            }


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


    private BusinessCompany convertDocumentToBusinessUser(Document item) {
        BusinessCompany user = new BusinessCompany(
                item.getString("name"),
                item.getString("emailAddress"),
                item.getString("address"),
                item.getString("category"),
                item.getString("phoneNumber")
        );
        user.setId(item.getObjectId("_id").toString());
        return user;
    }

    private MediaCompany convertDocumentToMediaCompanyUser(Document item) {
        MediaCompany user = new MediaCompany(
                item.getString("name"),
                item.getString("category"),
                item.getString("subcategory"),
                item.getString("emailAddress"),
                item.getString("address"),
                item.getString("phoneNumber")
        );
        user.setId(item.getObjectId("_id").toString());
        return user;
    }


} // end of main()
