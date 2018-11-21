package com.app.server.services;
import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.http.utils.APPCrypt;
import com.app.server.models.Product;
import com.app.server.util.MongoPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
public class ProductService {
    private static ProductService self;
    private ObjectWriter ow;
    private MongoCollection<Document> productCollection = null;
    private ProductService() {
        this.productCollection = MongoPool.getInstance().getCollection("product");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }
    public static ProductService getInstance(){
        if (self == null)
            self = new ProductService();
        return self;
    }
    public ArrayList<Product> getAll() {
        ArrayList<Product> productList = new ArrayList<Product>();
        FindIterable<Document> results = this.productCollection.find();
        if (results == null) {
            return productList;
        }
        for (Document item : results) {
            Product product = convertDocumentToProduct(item);
            productList.add(product);
        }
        return productList;
    }
    public Product getOne(String id, String pid) {
        BasicDBObject query = new BasicDBObject();
        query.put("businessId", id);
        query.put("_id", new ObjectId(pid));
        Document item = productCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToProduct(item);
    }
    public Product create(String businessId, Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Product product = convertJsonToProduct(businessId, json);
            Document doc = convertProductToDocument(product);
            productCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            product.setId(id.toString());
            return product;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            return null;
        }
    }
    public Object update(String bid, String pid, Object request) {
        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(pid));
            query.put("businessId", bid);
            Document doc = new Document();
            if (json.has("name"))
                doc.append("name",json.getString("name"));
            if (json.has("startPrice"))
                doc.append("startPrice",json.getString("startPrice"));
            if (json.has("endPrice"))
                doc.append("endPrice",json.getString("endPrice"));
            Document set = new Document("$set", doc);
            productCollection.updateOne(query,set);
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
    public Object delete(String bid, String pid) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(pid));
        query.put("businessId", bid);
        productCollection.deleteOne(query);
        return new JSONObject();
    }
    public Object deleteAll() {
        productCollection.deleteMany(new BasicDBObject());
        return new JSONObject();
    }
    private Product convertDocumentToProduct(Document item) {
        Product product = new Product(
                item.getString("name"),
                item.getString("startPrice"),
                item.getString("endPrice"),
                item.getString("businessId")
        );
        product.setId(item.getObjectId("_id").toString());
        return product;
    }
    private Document convertProductToDocument(Product product){
        Document doc = new Document("name", product.getName())
                .append("startPrice", product.getStartPrice())
                .append("endPrice", product.getEndPrice())
                .append("businessId",product.getBusinessId())
                ;
        return doc;
    }
    private Product convertJsonToProduct(String businessId, JSONObject json){
        Product product = new Product( json.getString("name"),
                json.getString("startPrice"),
                json.getString("endPrice"),
                businessId
        );
        return product;
    }
    public List<Product> getAllProductsInBusiness(HttpHeaders headers, String businessId) throws Exception {

        if (!(checkAuthentication(headers,businessId)))
            return null;

        BasicDBObject query = new BasicDBObject();
        query.put("businessId", businessId);
        List<Product> products = new ArrayList<>();
        FindIterable<Document> result = productCollection.find(query);
        //        MongoCursor<Document> iterator = productCollection.find(query).iterator();
//
//        BasicDBList list = new BasicDBList();
//        while (iterator.hasNext()) {
//            Document doc = iterator.next();
//            list.add(doc);
//        }
//        System.out.println(JSON.serialize(list));
        result.forEach((Block<Document>) document -> {
            products.add(convertDocumentToProduct(document));
        });
        return products;
    }

    //Authorization check
    boolean checkAuthentication(HttpHeaders headers, String id) throws Exception{
        List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            throw new APPUnauthorizedException(70,"No Authorization Headers");
        String token = authHeaders.get(0);
        String clearToken = APPCrypt.decrypt(token);
        if (id.compareTo(clearToken) != 0) {
            throw new APPUnauthorizedException(71,"Invalid token. Please try getting a new token");
        }
        return true;
    }
}