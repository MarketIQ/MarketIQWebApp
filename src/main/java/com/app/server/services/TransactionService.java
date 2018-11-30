package com.app.server.services;

import com.app.server.http.exceptions.APPBadRequestException;
import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.models.PaymentDetails;
import com.app.server.models.ThirdPartyPayment;
import com.app.server.models.Transaction;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;

public class TransactionService {

    private static TransactionService self;
    private ObjectWriter ow;
    private MongoCollection<Document> transactionCollection = null;
    private ThirdPartyPaymentService thirdPartyService;

    private TransactionService() {
        this.transactionCollection = MongoPool.getInstance().getCollection("transaction");
        thirdPartyService = ThirdPartyPaymentService.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static TransactionService getInstance(){
        if (self == null)
            self = new TransactionService();
        return self;
    }

    public Transaction getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        Document item = transactionCollection.find(query).first();
        if (item == null) {
            return  null;
        }
        return convertDocumentToTransaction(item);
    }

    public Transaction create(Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Transaction transaction = convertJsonToTransaction(json);

            //make a transaction object
            //call thirdparty service

            ThirdPartyPayment thirdPartyPayment = new ThirdPartyPayment(
                    new ObjectId(transaction.getPaymentDetails().getEmailAddress()).toString(),
                    transaction.getPaymentDetails().getPayment_id(), transaction.getPaymentDetails().getAmount(),  String.valueOf(Instant.now().getEpochSecond()),
                    transaction.getPaymentDetails().getEmailAddress());

            try {
                thirdPartyPayment = startTransaction(thirdPartyPayment);
                //TO-DO: Change to HTTP Post request
                //thirdPartyPayment = thirdPartyService.makePayment(thirdPartyPayment);
                transaction.setTransactionState("SUCCESSFUL");
                transaction.setTransactionTime(thirdPartyPayment.getPaymentTime());
            } catch (APPInternalServerException e){
                transaction.getPaymentDetails().setAmount(0.0);
                transaction.setTransactionState("FAILED");
            }
            // if we set id in document, will mongo set another id field?
            Document doc = convertTransactionToDocument(transaction);

            transactionCollection.insertOne(doc);
            //find the field inserted and find its id.
            ObjectId id = (ObjectId)doc.get( "_id" );
            transaction.setId(id.toString());
            return transaction;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        } catch(APPBadRequestException e) {
            throw new APPBadRequestException(33, e.getMessage());
        } catch(APPUnauthorizedException e) {
            throw new APPUnauthorizedException(34, e.getMessage());
        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99, e.getMessage());
        }
    }

    private ThirdPartyPayment startTransaction(ThirdPartyPayment thirdPartyPayment) throws Exception{
        //String target = "http://" + service.getHost() + ":" + service.getPort() + PROFILES_URI;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(thirdPartyPayment);

        StringEntity entity = new StringEntity(json,
                ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        //TO-DO: URL hardcoded, make it dynamic
        HttpPost request = new HttpPost("http://localhost:8080/api/thirdpartypayment");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);
        HttpEntity entityVal = response.getEntity();
        String content = EntityUtils.toString(entityVal);

        ObjectMapper mapper = new ObjectMapper();
        ThirdPartyPayment thirdParty = mapper.readValue(content, ThirdPartyPayment.class);
        return  thirdParty;
    }

    public ArrayList<Transaction> getAllUserTranscations(String id){
        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();

        FindIterable<Document> results = this.transactionCollection.find();
        if (results == null) {
            return transactionsList;
        }
        for (Document item : results) {
            Transaction transaction = convertDocumentToTransaction(item);
            if(transaction.getId().equals(id)){
                transactionsList.add(transaction);}
        }
        return transactionsList;
    }


    public Object update(String id, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("cardNumber"))
                doc.append("cardNumber",json.getString("cardNumber"));
            if (json.has("cardName"))
                doc.append("cardName",json.getString("cardName"));
            if (json.has("amount"))
                doc.append("amount",json.getString("amount"));
            if (json.has("amount"))
                doc.append("amount",json.getDouble("amount"));
            if (json.has("subscriptionPeriod"))
                doc.append("subscriptionPeriod",json.getString("subscriptionPeriod"));
            if (json.has("transactionTime"))
                doc.append("transactionTime",json.getString("transactionTime"));
            if (json.has("transactionState"))
                doc.append("transactionState",json.getString("transactionState"));

            Document set = new Document("$set", doc);
            transactionCollection.updateOne(query,set);
            return request;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        } catch(APPBadRequestException e) {
            throw new APPBadRequestException(33, e.getMessage());
        } catch(APPUnauthorizedException e) {
            throw new APPUnauthorizedException(34, e.getMessage());
        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99, e.getMessage());
        }
    }

    public Object delete(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        transactionCollection.deleteOne(query);
        return new JSONObject();
    }

    private Document convertTransactionToDocument(Transaction transaction){
        Document doc = new Document("emailAddress", transaction.getPaymentDetails().getEmailAddress())
                .append("cardNumber", transaction.getPaymentDetails().getCardNumber())
                .append("cardName", transaction.getPaymentDetails().getCardName())
                .append("amount", transaction.getPaymentDetails().getAmount())
                .append("subscriptionPeriod", transaction.getPaymentDetails().getSubscriptionPeriod())
                .append("transactionState", transaction.getTransactionState())
                .append("transactionTime", transaction.getTransactionTime())
                ;
        return doc;
    }


    private Transaction convertDocumentToTransaction(Document item) {
        PaymentDetails paymentDetails = new PaymentDetails(
                item.getString("emailAddress"),
                item.getString("cardNumber"),
                item.getString("cvvNumber"),
                item.getString("expiryDate"),
                item.getString("cardName"),
                new Double(item.getString("amount")),
                item.getString("subscriptionPeriod")
        );

        Transaction transaction = new Transaction(
                item.getString("emailAddress"),
                paymentDetails,
                item.getString("transactionState"),
                item.getString("transactionTime")
        );
        transaction.setId(item.getObjectId("_id").toString());
        return transaction;
    }

    private Transaction convertJsonToTransaction(JSONObject json){
        PaymentDetails paymentDetails = new PaymentDetails(
                json.getString("emailAddress"),
                json.getString("cardNumber"),
                json.getString("cvvNumber"),
                json.getString("expiryDate"),
                json.getString("cardName"),
                new Double(json.getString("amount")),
                json.getString("subscriptionPeriod")
        );

        Transaction transaction = new Transaction(
                json.getString("emailAddress"),
                paymentDetails,
                json.getString("transactionState"),
                json.getString("transactionTime")
        );
        //transaction.setId(json.getObjectId("_id").toString());
        return transaction;
    }
}