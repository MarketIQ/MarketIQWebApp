//package com.app.server.services;


/**
 * Services run as singletons
 */
/*
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

    public ArrayList<WishlistBusinessCompany> getAll() {


        ArrayList<WishlistBusinessCompany> wishListList = new ArrayList<WishlistBusinessCompany>();

        FindIterable<Document> results = wishListCollection.find();
        if (results == null) {
            return wishListList;
        }
        for (Document item : results) {
            WishlistBusinessCompany wishlist = convertDocumentToWishList(item);
            wishListList.add(wishlist);
        }
        return wishListList;
    }

    public WishlistBusinessCompany getOne(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        Document item = wishListCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToWishList(item);
    }

    public WishlistBusinessCompany create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            WishlistBusinessCompany wishlist = convertJsonToWishList(json);
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

    private WishlistBusinessCompany convertDocumentToWishList(Document item) {
        WishlistBusinessCompany wishlist = new WishlistBusinessCompany(
                item.getString("businessId"),
                item.getString("wishListEntry"),
                item.getString("creationDate")
        );
        wishlist.setWishListId(item.getObjectId("_id").toString());
        return wishlist;
    }

    private Document convertWishListToDocument(WishlistBusinessCompany wishlist){
        Document doc = new Document
                ("businessId", wishlist.getBusinessId())
                .append("wishListEntry", wishlist.getWishListEntry())
                .append("creationDate", wishlist.getCreationDate());
        return doc;
    }

    private WishlistBusinessCompany convertJsonToWishList(JSONObject json){
        WishlistBusinessCompany wishlist = new WishlistBusinessCompany( json.getString("businessId"),
                json.getString("wishListEntry"),
                json.getString("creationDate")
        );

        return wishlist;
    }




} // end of main()

*/