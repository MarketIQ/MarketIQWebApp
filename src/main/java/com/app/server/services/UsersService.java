//package com.app.server.services;

//import com.app.server.models.Users;


/**
 * Services run as singletons
 */
/*
public class UsersService {

    private static UsersService self;
    private ObjectWriter ow;

    private MongoCollection<Document> usersCollection;

    private UsersService() {
        this.usersCollection = MongoPool.getInstance().getCollection("users");

        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    public static UsersService getInstance(){
        if (self == null)
            self = new UsersService();
        return self;
    }

    public ArrayList<Users> getAll() {

        ArrayList<Users> userList = new ArrayList<Users>();

        FindIterable<Document> results = usersCollection.find();
        if (results == null) {
            return userList;
        }
        for (Document item : results) {
            Users user = convertDocumentToUser(item);
            userList.add(user);
        }
        return userList;
    }

    public Users getOne(HttpHeaders headers,String id) {
        try {
            checkAuthentication(headers, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BasicDBObject query = new BasicDBObject();
        query.put("_id" , new ObjectId(id));

        Document item = usersCollection.find(query).first();
        if (item == null) {
            return null;
        }
        return convertDocumentToUser(item);
    }


    public Users create(Object request) {

        try {
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));
            Users user = convertJsonToUser(json);
            Document doc = convertUserToDocument(user);
            usersCollection.insertOne(doc);
            ObjectId id = (ObjectId)doc.get( "_id" );
            user.setUserId(id.toString());
            return user;
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
                if (json.has("userName"))
                    doc.append("userName",json.getString("userName"));
                if (json.has("userRole"))
                    doc.append("userRole",json.getString("userRole"));
                if (json.has("userEmailId"))
                    doc.append("userEmailId",json.getString("userEmailId"));


                Document set = new Document("$set", doc);
                usersCollection.updateOne(query,set);
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

        usersCollection.deleteOne(query);

        return new JSONObject();
    }


    public Object deleteAll() {

        usersCollection.deleteMany(new BasicDBObject());

        return new JSONObject();
    }

    private Users convertDocumentToUser(Document item) {
        Users user = new Users(
                item.getString("userName"),
                item.getString("userBusinessId"),
                item.getString("userRole"),
                item.getString("userEmailId")
        );
        user.setUserId(item.getObjectId("_id").toString());
        return user;
    }

    private Document convertUserToDocument(Users user){
        Document doc = new Document("userName", user.getUserName())
                .append("userBusinessId", user.getUserBusinessId())
                .append("userRole", user.getUserRole())
                .append("userEmailId", user.getUserEmailId());
        return doc;
    }

    private Users convertJsonToUser(JSONObject json){
        Users user = new Users( json.getString("userName"),
                json.getString("userBusinessId"),
                json.getString("userRole"),
                json.getString("userEmailId")
        );

        return user;
    }

    void checkAuthentication(HttpHeaders headers, String id) throws Exception{
        List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            throw new APPUnauthorizedException(70,"No Authorization Headers");
        String token = authHeaders.get(0);
        String clearToken = APPCrypt.decrypt(token);
        if (id.compareTo(clearToken) != 0) {
            throw new APPUnauthorizedException(71,"Invalid token. Please try getting a new token");
        }
    }




} // end of main()

*/