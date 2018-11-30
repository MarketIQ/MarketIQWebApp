package com.app.server.services;

import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.models.Wishlist;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WishlistService {

    private ObjectWriter ow;
    private MongoCollection<Document> businessCompanyCollection = null;
    private MongoCollection<Document> wishListCollection;


    public Wishlist getOneWishList(String emailAddress) {

        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", emailAddress);

        Document item_wl = wishListCollection.find(query).first();
        if (item_wl == null) {
            return null;
        }
        Wishlist wl = convertDocumentToWishList(item_wl);
        return wl;
    }

    public ArrayList<Wishlist> getWishList(String emailAddress) throws Exception {

        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", emailAddress);
        FindIterable<Document> documents = wishListCollection.find(query);

        if (documents == null) {
            return null;
        }

        ArrayList<Wishlist> results = new ArrayList<>();
        for (Document item : documents) {
            Wishlist wishlistBusinessCompany = convertDocumentToWishList(item);
            results.add(wishlistBusinessCompany);
        }

        return results;
    }

    public Wishlist createWishList(String emailAddress, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            BasicDBObject query = new BasicDBObject();
            query.put("emailAddress", emailAddress);
            Wishlist wishlistFromDb = this.getOneWishList(emailAddress);
            List<String> wishlistCompanies;
            if (wishlistFromDb == null) {
                wishlistCompanies = new ArrayList<>();
                wishlistCompanies.add(json.getString("wishlistCompany"));
                wishlistFromDb = new Wishlist(emailAddress, wishlistCompanies);
            } else {
                wishlistCompanies = wishlistFromDb.getWishListCompanies();
                wishlistCompanies.add(json.getString("wishlistCompany"));
                wishlistFromDb.setWishListCompanies(wishlistCompanies);
            }
            Document docToWrite = convertWishListToDocument(wishlistFromDb);

            wishListCollection.findOneAndReplace(query, docToWrite);
            return wishlistFromDb;

        } catch (JsonProcessingException | APPUnauthorizedException e) {
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

    private Wishlist convertDocumentToWishList(Document item) {

        List<String> wishlistCompanies = ((List<String>) item.get("wishListCompanies"));
        Wishlist wl = new Wishlist(
                item.getString("emailAddress"),
                wishlistCompanies
        );
        return wl;
    }

//    private Wishlist convertJsonToWishList(JSONObject json, String emailAddress) {
//
//        Wishlist wishlist = new Wishlist(
//                emailAddress,
//                json.getString("wishListCompany")
//        );
//
//        return wishlist;
//    }

    private Document convertWishListToDocument(Wishlist wishlist) {
        Document doc = new Document("emailAddress", wishlist.getEmailAddress())
                .append("wishListCompanies", wishlist.getWishListCompanies());
        return doc;
    }

    public Object updateWishlist(String emailAddress, Object request)  {

        try {
        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", emailAddress);
        Wishlist wishlistFromDb = this.getOneWishList(emailAddress);
        List<String> wishlistCompanies = wishlistFromDb.getWishListCompanies();


        JSONObject json = null;
        json = new JSONObject(ow.writeValueAsString(request));

        String companyToDelete = json.getString("wishlistcompany");

        wishlistCompanies.remove(companyToDelete);

        Document docToWrite = convertWishListToDocument(wishlistFromDb);

        wishListCollection.findOneAndReplace(query, docToWrite);

        return wishlistFromDb;

        } catch (JSONException e) {
            System.out.println("Failed to update a document");
            return null;


        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

}
