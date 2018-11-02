package com.app.server.services;

import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.models.MediaCompany;
import com.app.server.models.WishList;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Services run as singletons
 */

public class MediaCompanyService {

    private static MediaCompanyService self;
    private ObjectWriter ow;
    private MongoCollection<Document> MediaCompanyCollection;
    private MongoCollection<Document> wishListCollection;

    private MediaCompanyService() {
        this.MediaCompanyCollection = MongoPool.getInstance().getCollection("MediaCompany");
        this.wishListCollection = MongoPool.getInstance().getCollection("wishlist");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static MediaCompanyService getInstance(){
        if (self == null)
            self = new MediaCompanyService();
        return self;
    }

    public ArrayList<MediaCompany> getAll() {
        ArrayList<MediaCompany> MediaCompanyList = new ArrayList<MediaCompany>();

        FindIterable<Document> results = MediaCompanyCollection.find();
        if (results == null) {
            return MediaCompanyList;
        }
        for (Document item : results) {
            MediaCompany mediacompany = convertDocumentToMediaCompany(item);
            MediaCompanyList.add(mediacompany);
        }
        return MediaCompanyList;
    }

    public MediaCompany getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = MediaCompanyCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToMediaCompany(item);
    }

    public MediaCompany create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            MediaCompany MediaCompany = convertJsonToMediaCompany(json);
            Document doc = convertMediaCompanyToDocument(MediaCompany);
            MediaCompanyCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            MediaCompany.setMediaCompanyId(id.toString());
            return MediaCompany;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }


    public Object update(String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("mediaCompanyName"))
                doc.append("mediaCompanyName",json.getString("mediaCompanyName"));
            if (json.has("mediaCompanyId"))
                doc.append("mediaCompanyId",json.getString("mediaCompanyId"));
            if (json.has("mediaCompanyPhone"))
                doc.append("mediaCompanyPhone",json.getString("mediaCompanyPhone"));

            Document set = new Document("$set", doc);
            MediaCompanyCollection.updateOne(query,set);
            return request;

        } catch(JSONException e) {
            System.out.println("Failed to update a document");
            return null;
        }
        catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        MediaCompanyCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        MediaCompanyCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private MediaCompany convertDocumentToMediaCompany(Document item) {
        MediaCompany mediacompany = new MediaCompany(
                item.getString("mediaCompanyName"),
                item.getString("mediaCompanyEmailId"),
                item.getString("mediaCompanyPhone")
        );
        mediacompany.setMediaCompanyId(item.getObjectId("_id").toString());
        return mediacompany;
    }

    private Document convertMediaCompanyToDocument(MediaCompany mediacompany){
        Document doc = new Document
                ("mediaCompanyName", mediacompany.getMediaCompanyName())
                .append("mediaCompanyEmailId", mediacompany.getMediaCompanyEmailId())
                .append("mediaCompanyPhone", mediacompany.getMediaCompanyPhone());
        return doc;
    }

    private MediaCompany convertJsonToMediaCompany(JSONObject json){
        MediaCompany mediacompany =  new MediaCompany( json.getString("mediaCompanyName"),
                json.getString("mediaCompanyEmailId"),
                json.getString("mediaCompanyPhone")
        );

        return mediacompany;
    }

    //get one wishlist
    public WishList getOneWishList(String id, String sid) {

        BasicDBObject query = new BasicDBObject();
        query.put("businessId" , id);
        query.put("_id" , new ObjectId(sid));

        Document item_wl = wishListCollection.find(query).first();
        if (item_wl == null) {
            return null;
        }
        WishList wl = convertDocumentToWishList(item_wl);
        return wl;
    }

    public ArrayList<WishList> getWishList(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("businessId", id);
        FindIterable<Document> documents = wishListCollection.find(query);

        if (documents == null) {
            return null;
        }

        ArrayList<WishList> results = new ArrayList<>();
        for (Document item : documents) {
            WishList wishList = convertDocumentToWishList(item);
            results.add(wishList);
        }

        return results;
    }

    public WishList createWishList(String bid,Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            WishList wishlist = convertJsonToWishList(json,bid);
            Document doc = convertWishListToDocument(wishlist);
            wishListCollection.insertOne(doc);

            ObjectId id = (ObjectId) doc.get("_id");
            wishlist.setWishListId(id.toString());
            return wishlist;

        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public Object deletewishlist(String sid) {

        BasicDBObject query = new BasicDBObject();

        query.put("_id", new ObjectId(sid));

        wishListCollection.deleteOne(query);

        return new JSONObject();
    }
    private WishList convertDocumentToWishList(Document item) {

        WishList wl = new WishList(

                item.getString("businessId"),
                item.getString("wishListEntry"),
                item.getString("creationDate")
        );
        wl.setWishListId(item.getObjectId("_id").toString());
        return wl;
    }
    private WishList convertJsonToWishList(JSONObject json,String bid){
        WishList wishlist = new WishList(
                bid,
                json.getString("wishListEntry"),
                json.getString("creationDate")
        );

        return wishlist;
    }
    private Document convertWishListToDocument(WishList wishlist){
        Document doc = new Document("businessId", wishlist.getBusinessId())
                .append("wishListEntry", wishlist.getWishListEntry())
                .append("creationDate", wishlist.getCreationDate());
        return doc;
    }

    public Object updatewishlist(String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("businessId"))
                doc.append("businessId",json.getString("businessId"));
            if (json.has("wishListEntry"))
                doc.append("wishListEntry",json.getString("wishListEntry"));
            if (json.has("creationDate"))
                doc.append("creationDate",json.getString("creationDate"));


            Document set = new Document("$set", doc);
            wishListCollection.updateOne(query,set);
            return request;

        } catch(JSONException e) {
            System.out.println("Failed to update a document");
            return null;


        }
        catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

} // end of main()

