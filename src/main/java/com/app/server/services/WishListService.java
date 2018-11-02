package com.app.server.services;

import com.app.server.models.WishList;
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


/**
 * Services run as singletons
 */

public class WishListService {

    private static WishListService self;
    private ObjectWriter ow;
    private MongoCollection<Document> wishListCollection;


    private WishListService() {
        this.wishListCollection = MongoPool.getInstance().getCollection("wishlist");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static WishListService getInstance(){
        if (self == null)
            self = new WishListService();
        return self;
    }

    public ArrayList<WishList> getAll() {
        ArrayList<WishList> wishListList = new ArrayList<WishList>();

        FindIterable<Document> results = wishListCollection.find();
        if (results == null) {
            return wishListList;
        }
        for (Document item : results) {
            WishList wishlist = convertDocumentToWishList(item);
            wishListList.add(wishlist);
        }
        return wishListList;
    }

    public WishList getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = wishListCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToWishList(item);
    }

    public WishList create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            WishList wishlist = convertJsonToWishList(json);
            Document doc = convertWishListToDocument(wishlist);
            wishListCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            wishlist.setWishListId(id.toString());
            return wishlist;
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




    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        wishListCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        wishListCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private WishList convertDocumentToWishList(Document item) {
        WishList wishlist = new WishList(
                item.getString("businessId"),
                item.getString("wishListEntry"),
                item.getString("creationDate")
        );
        wishlist.setWishListId(item.getObjectId("_id").toString());
        return wishlist;
    }

    private Document convertWishListToDocument(WishList wishlist){
        Document doc = new Document
                ("businessId", wishlist.getBusinessId())
                .append("wishListEntry", wishlist.getWishListEntry())
                .append("creationDate", wishlist.getCreationDate());
        return doc;
    }

    private WishList convertJsonToWishList(JSONObject json){
        WishList wishlist = new WishList( json.getString("businessId"),
                json.getString("wishListEntry"),
                json.getString("creationDate")
        );

        return wishlist;
    }




} // end of main()

