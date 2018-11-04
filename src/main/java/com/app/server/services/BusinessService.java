package com.app.server.services;
import com.app.server.models.Business;
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
import com.app.server.models.WishList;
/**
 * Services run as singletons
 */
public class BusinessService {
    private static BusinessService self;
    private ObjectWriter ow;
    private MongoCollection<Document> businessCollection = null;
    private MongoCollection<Document> wishListCollection;
    private BusinessService() {
        this.businessCollection = MongoPool.getInstance().getCollection("business");
        this.wishListCollection = MongoPool.getInstance().getCollection("wishlist");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }
    public static BusinessService getInstance(){
        if (self == null)
            self = new BusinessService();
        return self;
    }
    public ArrayList<Business> getAll() {
        ArrayList<Business> businessList = new ArrayList<Business>();
        FindIterable<Document> results = this.businessCollection.find();
        if (results == null) {
            return businessList;
        }
        for (Document item : results) {
            Business business = convertDocumentToBusiness(item);
            businessList.add(business);
        }
        return businessList;
    }
    public Business getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        Document item = businessCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToBusiness(item);
    }
    public Business create(Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Business business = convertJsonToBusiness(json);
            Document doc = convertBusinessToDocument(business);
            businessCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            business.setId(id.toString());
            return business;
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
            if (json.has("name"))
                doc.append("name",json.getString("name"));
            if (json.has("address"))
                doc.append("address",json.getString("address"));
            if (json.has("type"))
                doc.append("type",json.getString("type"));
            Document set = new Document("$set", doc);
            businessCollection.updateOne(query,set);
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
        businessCollection.deleteOne(query);
        return new JSONObject();
    }
    public Object deleteAll() {
        businessCollection.deleteMany(new BasicDBObject());
        return new JSONObject();
    }
    private Business convertDocumentToBusiness(Document item) {
        Business business = new Business(
                item.getString("name"),
                item.getString("address"),
                item.getString("type")
        );
        business.setId(item.getObjectId("_id").toString());
        return business;
    }
    private Document convertBusinessToDocument(Business business){
        Document doc = new Document("name", business.getName())
                .append("address", business.getAddress())
                .append("type", business.getType())
                ;
        return doc;
    }
    private Business convertJsonToBusiness(JSONObject json){
        Business business = new Business( json.getString("name"),
                json.getString("address"),
                json.getString("type"));
        return business;
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