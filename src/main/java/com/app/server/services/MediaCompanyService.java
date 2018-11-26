package com.app.server.services;

import com.app.server.http.exceptions.APPConflictException;
import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.http.utils.APPCrypt;
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

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Services run as singletons
 */

public class MediaCompanyService {

    private static MediaCompanyService self;
    private ObjectWriter ow;
    private MongoCollection<Document> MediaCompanyCollection;
    private MongoCollection<Document> wishListCollection;
    private MongoCollection<Document> paymentCollection;


    private MediaCompanyService() {
        this.MediaCompanyCollection = MongoPool.getInstance().getCollection("mediacompany");
        this.wishListCollection = MongoPool.getInstance().getCollection("wishlistMediaCompany");
        this.paymentCollection = MongoPool.getInstance().getCollection("payments");


        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }

    public static MediaCompanyService getInstance() {
        if (self == null)
            self = new MediaCompanyService();
        return self;
    }

    public ArrayList<MediaCompany> getAll(String category, String subCategory) {
        BasicDBObject query = new BasicDBObject();

        if (category == null) {
            return null;
        }
        query.put("category", category);

        if (subCategory != null) {
            query.put("subCategory", subCategory);
        }

        ArrayList<MediaCompany> MediaCompanyList = new ArrayList<MediaCompany>();
        FindIterable<Document> results = MediaCompanyCollection.find(query);

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

    public MediaCompany create(Object request) throws JsonProcessingException {

            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            String category = json.getString("category");
            String subCategory = json.getString("subCategory");
            String emailAddress = json.getString("emailAddress");


            BasicDBObject query = new BasicDBObject();
            query.put("category", category);
            query.put("subCategory", subCategory);
            query.put("emailAddress", emailAddress);

            if (MediaCompanyCollection.find(query).first() != null) {
                throw new APPConflictException(409, "media company with same " +
                        "category and sub category already exists");
            }

        try {
            MediaCompany mediaCompany = convertJsonToMediaCompany(json);
            Document doc = convertMediaCompanyToDocument(mediaCompany);
            MediaCompanyCollection.insertOne(doc);
            ObjectId id = (ObjectId) doc.get("_id");
            mediaCompany.setId(id.toString());
            return mediaCompany;
        }

        catch (Exception e) {

         throw new APPInternalServerException(500, "Failed to create a document");

        }
    }


    public Object update(String id, MediaCompany request) {
        try {

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            doc.append("name", request.getName());
            doc.append("category", request.getCategory());
            doc.append("subCategory", request.getSubCategory());
            doc.append("emailAddress", request.getEmailAddress());
            doc.append("address", request.getAddress());
            doc.append("phoneNumber", request.getPhoneNumber());


            Document set = new Document("$set", doc);
            MediaCompanyCollection.updateOne(query, set);
            return request;

        } catch (JSONException e) {
            System.out.println("Failed to update a document");
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
                item.getString("name"),
                item.getString("category"),
                item.getString("subCategory"),
                item.getString("emailAddress"),
                item.getString("address"),
                item.getString("phoneNumber")
        );
        mediacompany.setId(item.getObjectId("_id").toString());
        return mediacompany;
    }

    private Document convertMediaCompanyToDocument(MediaCompany mediacompany) {
        Document doc = new Document
                ("name", mediacompany.getName())
                .append("category", mediacompany.getCategory())
                .append("subCategory", mediacompany.getSubCategory())
                .append("emailAddress", mediacompany.getEmailAddress())
                .append("address", mediacompany.getAddress())
                .append("phoneNumber", mediacompany.getPhoneNumber());
        return doc;
    }

    private MediaCompany convertJsonToMediaCompany(JSONObject json) {
        MediaCompany mediacompany = new MediaCompany(json.getString("name"),
                json.getString("category"),
                json.getString("subCategory"),
                json.getString("emailAddress"),
                json.getString("address"),
                json.getString("phoneNumber")
        );

        return mediacompany;
    }

    //get one wishlist
    public WishlistMediaCompany getOneWishList(String id, String sid) throws Exception {

        //  if (!(checkAuthentication(headers,id)))
        //      return null;

        BasicDBObject query = new BasicDBObject();
        query.put("mediaCompanyId", id);
        query.put("_id", new ObjectId(sid));

        Document item_wl = wishListCollection.find(query).first();
        if (item_wl == null) {
            return null;
        }
        WishlistMediaCompany wl = convertDocumentToWishList(item_wl);
        return wl;
    }

    public ArrayList<WishlistMediaCompany> getWishList(String id) throws Exception {

        // if (!(checkAuthentication(headers,id)))
        //    return null;

        BasicDBObject query = new BasicDBObject();
        query.put("mediaCompanyId", id);
        FindIterable<Document> documents = wishListCollection.find(query);

        if (documents == null) {
            return null;
        }

        ArrayList<WishlistMediaCompany> results = new ArrayList<>();
        for (Document item : documents) {
            WishlistMediaCompany wishList = convertDocumentToWishList(item);
            results.add(wishList);
        }

        return results;
    }

    public WishlistMediaCompany createWishList(String bid, Object request) {


        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            WishlistMediaCompany wishlist = convertJsonToWishList(json, bid);
            Document doc = convertWishListToDocument(wishlist);
            wishListCollection.insertOne(doc);

            ObjectId id = (ObjectId) doc.get("_id");
            wishlist.setMediaCompanyWishListId(id.toString());
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

    private WishlistMediaCompany convertDocumentToWishList(Document item) {

        WishlistMediaCompany wl = new WishlistMediaCompany(

                item.getString("mediaCompanyId"),
                item.getString("wishListEntry"),
                item.getString("creationDate")
        );
        wl.setMediaCompanyWishListId(item.getObjectId("_id").toString());
        return wl;
    }

    private WishlistMediaCompany convertJsonToWishList(JSONObject json, String bid) {
        WishlistMediaCompany wishlist = new WishlistMediaCompany(
                bid,
                json.getString("wishListEntry"),
                json.getString("creationDate")
        );

        return wishlist;
    }

    private Document convertWishListToDocument(WishlistMediaCompany wishlist) {
        Document doc = new Document("mediaCompanyId", wishlist.getMediaCompanyId())
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
            if (json.has("mediaCompanyId"))
                doc.append("mediaCompanyId", json.getString("mediaCompanyId"));
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

    //Authorization check
    boolean checkAuthentication(HttpHeaders headers, String id) throws Exception {
        List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            throw new APPUnauthorizedException(70, "No Authorization Headers");
        String token = authHeaders.get(0);
        String clearToken = APPCrypt.decrypt(token);
        if (id.compareTo(clearToken) != 0) {
            throw new APPUnauthorizedException(71, "Invalid token. Please try getting a new token");
        }
        return true;
    }


    public PaymentDetails addPaymentDetails(Object request) throws JsonProcessingException {


        JSONObject json = null;
        json = new JSONObject(ow.writeValueAsString(request));
        PaymentDetails paymentDetails = convertJsonToPaymentDetails(json);
        String email = paymentDetails.getEmailAddress();

        //GetBusiness
        MediaCompany mediaCompany = getOne(email);
        if (mediaCompany == null) {
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


//    @POST
//    @Produces({ MediaType.APPLICATION_JSON})
//    public PaymentDetails register_subscription(HttpHeaders headers, Object request) {
//        try {
//
//            JSONObject json = null;
//            json = new JSONObject(ow.writeValueAsString(request));
//            PaymentDetails paymentDetails = convertJsonToSubscription(json);
//
//            //Check Authentication
//            //TO-DO : Uncomment the authentication
//            //checkAuthentication(headers, paymentDetails.getUserId());
//
//            //GetBusiness
//            MediaCompany mediacompany = getOne(paymentDetails.getemailId());
//
//            //CreateReferenceID for payments
//            //TO-DO : referenceID not being used, hence commented.
//            //String referenceID = APPCrypt.encrypt(business.getId()+PAYMENT);
//
//            //call paymentGateway Service
//            /*Transaction transaction = new Transaction(
//                    business.getId(),
//                    business.getCardName(),
//                    paymentDetails.getTransactionType(),
//                    paymentDetails.getAmount(),
//                    "USD",
//                    String.valueOf(Instant.now().getEpochSecond()),
//                    "REQUESTED");*/
//            Transaction transaction = new Transaction(
//                    mediacompany.getEmailAddress(),
//                    paymentDetails.getTransactionType(),
//                    paymentDetails.getAmount(),
//                    "USD",
//                    String.valueOf(Instant.now().getEpochSecond()),
//                    "REQUESTED");
//
//            try {
//                transaction = thirdPartyService.makePayment(transaction);
//                //update paymentDetails with response and persist
//                paymentDetails.setAmountCharged(transaction.getPaymentAmount());
//                paymentDetails.setAttemptState("SUCCESSFUL");
//                paymentDetails.setTransactionTime(transaction.getPaymentTime());
//            } catch (APPInternalServerException e){
//                paymentDetails.setAmountCharged("0");
//                paymentDetails.setAttemptState("FAILED");
//            }
//
//            Document subscr = convertSubscriptionToDocument(paymentDetails);
//            subscriptionCollection.insertOne(subscr);
//            ObjectId id = (ObjectId) subscr.get("_id");
//            paymentDetails.setId(id.toString());
//
//            return paymentDetails;
//        } catch (JsonProcessingException e) {
//            System.out.println("Failed to create a document");
//            throw new APPInternalServerException(33, "PaymentDetails Failed");
//        }  catch (java.lang.Exception e){
//            System.out.println("Failed to create a document");
//            throw new APPInternalServerException(33, "PaymentDetails Failed");
//        }
//    }
//
//    private Document convertSubscriptionToDocument(PaymentDetails paymentDetails){
//        Document doc = new Document("amount", paymentDetails.getAmount())
//                .append("amountCharged", paymentDetails.getAmountCharged())
//                .append("attemptCount", paymentDetails.getAttemptCount())
//                .append("attemptState", paymentDetails.getAttemptState())
//                .append("contractLength", paymentDetails.getContractLength())
//                .append("transactionTime", paymentDetails.getTransactionTime())
//                .append("transactionType", paymentDetails.getTransactionType())
//                .append("userId", paymentDetails.getemailId())
//                .append("UserType", paymentDetails.getUserType());
//
//        return doc;
//    }
//
//    private PaymentDetails convertJsonToSubscription(JSONObject json){
//        PaymentDetails paymentDetails = new PaymentDetails(
//                json.getString("emailId"),
//                json.getString("amount"),
//                json.getInt("contractLength"),
//                json.getString("userType"));
//        return paymentDetails;
//    }



} // end of main()

