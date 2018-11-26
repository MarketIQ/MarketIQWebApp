package com.app.server.services;

import com.app.server.http.exceptions.APPConflictException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.models.*;
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
import java.util.List;

/**
 * Services run as singletons
 */
public class BusinessCompanyService {
    private static BusinessCompanyService self;
    private ObjectWriter ow;
    private MongoCollection<Document> businessCompanyCollection = null;
    private MongoCollection<Document> wishListCollection;
    private MongoCollection<Document> paymentCollection;

    private BusinessCompanyService() {
        this.businessCompanyCollection = MongoPool.getInstance().getCollection("businesCompanies");
        this.wishListCollection = MongoPool.getInstance().getCollection("wishlists");
        this.paymentCollection = MongoPool.getInstance().getCollection("payments");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static BusinessCompanyService getInstance() {
        if (self == null)
            self = new BusinessCompanyService();
        return self;
    }

    public ArrayList<BusinessCompany> getAll() {

        ArrayList<BusinessCompany> businessCompanyList = new ArrayList<BusinessCompany>();
        FindIterable<Document> results = this.businessCompanyCollection.find();
        if (results == null) {
            return businessCompanyList;
        }
        for (Document item : results) {
            BusinessCompany businessCompany = convertDocumentToBusiness(item);
            businessCompanyList.add(businessCompany);
        }
        return businessCompanyList;
    }

    public ArrayList<BusinessCompany> getSome(String category) {

        BasicDBObject query = new BasicDBObject();
        if (category != null) {
            query.put("category", category);
        }
        ArrayList<BusinessCompany> businessCompanyList = new ArrayList<BusinessCompany>();
        FindIterable<Document> results = this.businessCompanyCollection.find(query);
        if (results == null) {
            return businessCompanyList;
        }
        for (Document item : results) {
            BusinessCompany businessCompany = convertDocumentToBusiness(item);
            businessCompanyList.add(businessCompany);
        }
        return businessCompanyList;
    }

    public BusinessCompany getOne(String emailAddress) {
        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", emailAddress);
        Document item = businessCompanyCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToBusiness(item);
    }

    public BusinessCompany create(Object request) throws JsonProcessingException {

        JSONObject json = null;
        json = new JSONObject(ow.writeValueAsString(request));
        String category = json.getString("category");
        String emailAddress = json.getString("emailAddress");


        BasicDBObject query = new BasicDBObject();
        query.put("category", category);
        query.put("emailAddress", emailAddress);

        if (businessCompanyCollection.find(query).first() != null) {
            throw new APPConflictException(409, "business with same category already exists");
        }


        BusinessCompany businessCompany = convertJsonToBusiness(json);
        Document doc = convertBusinessToDocument(businessCompany);
        businessCompanyCollection.insertOne(doc);
        ObjectId id = (ObjectId) doc.get("_id");
        businessCompany.setId(id.toString());
        return businessCompany;
    }

    public Object update(String emailAddress, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            BasicDBObject query = new BasicDBObject();
            query.put("emailAddress", emailAddress);
            query.put("emailAddress", emailAddress);
            Document doc = new Document();
            if (json.has("name"))
                doc.append("name", json.getString("name"));
            if (json.has("address"))
                doc.append("address", json.getString("address"));
            if (json.has("category"))
                doc.append("category", json.getString("category"));
            Document set = new Document("$set", doc);
            businessCompanyCollection.updateOne(query, set);
            return request;
        } catch (JSONException e) {
            System.out.println("Failed to update a document");
            return null;
        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }

    public Object delete(String emailAddress) {
        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", emailAddress);
        businessCompanyCollection.deleteOne(query);
        return new JSONObject();
    }

    public Object deleteAll() {
        businessCompanyCollection.deleteMany(new BasicDBObject());
        return new JSONObject();
    }

    private BusinessCompany convertDocumentToBusiness(Document item) {
        BusinessCompany businessCompany = new BusinessCompany(
                item.getString("name"),
                item.getString("emailAddress"),
                item.getString("address"),
                item.getString("category"),
                item.getString("phoneNumber")
        );
        businessCompany.setId(item.getObjectId("_id").toString());
        return businessCompany;
    }

    private Document convertBusinessToDocument(BusinessCompany businessCompany) {
        Document doc = new Document("name", businessCompany.getName())
                .append("emailAddress", businessCompany.getEmailAddress())
                .append("address", businessCompany.getAddress())
                .append("category", businessCompany.getCategory())
                .append("phoneNumber", businessCompany.getPhoneNumber());

        return doc;
    }

    private BusinessCompany convertJsonToBusiness(JSONObject json) {
        BusinessCompany businessCompany = new BusinessCompany(
                json.getString("name"),
                json.getString("emailAddress"),
                json.getString("address"),
                json.getString("category"),
                json.getString("phoneNumber"));
        return businessCompany;
    }

    //get one wishlist
    public WishlistBusinessCompany getOneWishList(String emailAddress, String sid) {

        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", emailAddress);
        query.put("_id", new ObjectId(sid));

        Document item_wl = wishListCollection.find(query).first();
        if (item_wl == null) {
            return null;
        }
        WishlistBusinessCompany wl = convertDocumentToWishList(item_wl);
        return wl;
    }

    public ArrayList<WishlistBusinessCompany> getWishList(String emailAddress) throws Exception {

        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", emailAddress);
        FindIterable<Document> documents = wishListCollection.find(query);

        if (documents == null) {
            return null;
        }

        ArrayList<WishlistBusinessCompany> results = new ArrayList<>();
        for (Document item : documents) {
            WishlistBusinessCompany wishlistBusinessCompany = convertDocumentToWishList(item);
            results.add(wishlistBusinessCompany);
        }

        return results;
    }

    public WishlistBusinessCompany createWishList(String emailAddress, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            WishlistBusinessCompany wishlist = convertJsonToWishList(json, emailAddress);
            Document doc = convertWishListToDocument(wishlist);
            wishListCollection.insertOne(doc);

            ObjectId id = (ObjectId) doc.get("_id");
            wishlist.setWishListId(id.toString());
            return wishlist;

        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        } catch (APPUnauthorizedException e) {
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

    private WishlistBusinessCompany convertDocumentToWishList(Document item) {

        WishlistBusinessCompany wl = new WishlistBusinessCompany(

                item.getString("emailAddress"),
                item.getString("wishListEntry"),
                item.getString("creationDate")
        );
        wl.setWishListId(item.getObjectId("_id").toString());
        return wl;
    }

    private WishlistBusinessCompany convertJsonToWishList(JSONObject json, String emailAddress) {
        WishlistBusinessCompany wishlist = new WishlistBusinessCompany(
                emailAddress,
                json.getString("wishListEntry"),
                json.getString("creationDate")
        );

        return wishlist;
    }

    private Document convertWishListToDocument(WishlistBusinessCompany wishlist) {
        Document doc = new Document("emailAddress", wishlist.getEmailAddress())
                .append("wishListEntry", wishlist.getWishListEntry())
                .append("creationDate", wishlist.getCreationDate());
        return doc;
    }

    public Object updateWishlist(String emailAddress, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("emailAddress", emailAddress);

            Document doc = new Document();
            if (json.has("emailAddress"))
                doc.append("emailAddress", json.getString("emailAddress"));
            if (json.has("wishListEntry"))
                doc.append("wishListEntry", json.getString("wishListEntry"));
            if (json.has("creationDate"))
                doc.append("creationDate", json.getString("creationDate"));


            Document set = new Document("$set", doc);
            wishListCollection.updateOne(query, set);
            return request;

        } catch (JSONException e) {
            System.out.println("Failed to update a document");
            return null;


        } catch (JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }


    public PaymentDetails addPaymentDetails(Object request) throws JsonProcessingException {


        JSONObject json = null;
        json = new JSONObject(ow.writeValueAsString(request));
        PaymentDetails paymentDetails = convertJsonToPaymentDetails(json);
        String email = paymentDetails.getEmailAddress();

        //GetBusiness
        BusinessCompany business = getOne(email);
        if (business == null) {
            throw new APPNotFoundException(404, "EmailAddress mismmatch error!!!");

        }

        BasicDBObject query = new BasicDBObject();
        query.put("emailAddress", email);
        Document document = paymentCollection.find(query).first();
        PaymentDetails existingPaymentDetails=new PaymentDetails();
        if (document == null) {
            Document paymentDoc = convertPaymentDetailsToDocument(paymentDetails);
            paymentCollection.insertOne(paymentDoc);
        } else {
            existingPaymentDetails = convertDocumentToPaymentDetails(document);
            existingPaymentDetails.getPaymentMethodList().add(paymentDetails.getPaymentMethodList().get(0));

            Document paymentDoc = convertPaymentDetailsToDocument(existingPaymentDetails);
            paymentCollection.insertOne(paymentDoc);
        }
        return existingPaymentDetails;


    }

    private Document convertPaymentDetailsToDocument(PaymentDetails paymentDetails) {
        Document doc = new Document("emailAddress", paymentDetails.getEmailAddress())
                .append("paymentMethods", paymentDetails.getPaymentMethodList());
        return doc;
    }


    private PaymentDetails convertJsonToPaymentDetails(JSONObject json) {

        List<PaymentMethod> paymentMethods = (List<PaymentMethod>) json.getJSONObject("paymentMethods");
        PaymentDetails paymentDetails = new PaymentDetails(
                json.getString("emailAddress"), paymentMethods
        );

        return paymentDetails;
    }

    private PaymentDetails convertDocumentToPaymentDetails(Document item) {

        PaymentDetails paymentDetails = new PaymentDetails(

                item.getString("emailAddress"),
                (List<PaymentMethod>) item.get("paymentMethods")
        );
        //paymentDetails.set(item.getObjectId("_id").toString());
        return paymentDetails;
    }


} // end of main()