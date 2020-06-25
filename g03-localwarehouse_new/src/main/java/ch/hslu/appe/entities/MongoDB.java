package ch.hslu.appe.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoDB {
    private static final LocalWarehouse localWarehouse = LocalWarehouse.getInstance();
    private static String databaseName = localWarehouse.getName();
    private static Random rand = new Random();
    private static Gson gson = new Gson();
    private static String collectionName;
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection <Document> collection;
    private String objectId;
    @JsonProperty()
    private static Map<String, String> mapFromArticleJson = new HashMap <>();


    private static final Logger LOG = LoggerFactory.getLogger(MongoDB.class);



    public static void setUpConnection(String databaseName){
        //localWarehouse.init(99, "DemoInsertion");
        if(OSValidator.isWindows()){
            mongoClient = new MongoClient(new MongoClientURI("mongodb://192.168.99.100:27018"));
        }
        else if(OSValidator.isUnix()){
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localwarehouse_mongo:27017"));
        }
        else{
            System.out.println("OSX");
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27018"));
        }
        database = mongoClient.getDatabase(databaseName);
        MongoDB.setCollectionName(databaseName);
        collection = database.getCollection(getCollectionName());
    }

    public static void insertDocument(Document articleDocument){
        //System.out.println(articleDocument);
        collection.insertOne(articleDocument);
    }

    public static Document getArticle(String id){
        System.out.printf(collectionName);
        Document foundDocumentById = collection.find(new Document("_id", id)).first();
        return foundDocumentById;
    }

    public static List<Document> getAllArticles(){
        List<Document> articlesList = collection.find().into(new ArrayList<>());
        return articlesList;
    }

    public static void updateDocument(Document document){
        collection.replaceOne(Filters.eq("_id", document.get("_id")), document);
    }

    public static void updateDocumentNumber(Document document){
        collection.replaceOne(Filters.eq("_id", document.get("_id")), document);
    }

    public static void deleteCollection(String collectionName){
        collection.drop();
    }

    public static String getCollectionName() {
        return collectionName;
    }

    public static void setCollectionName(String collectionName) {
        MongoDB.collectionName = collectionName;
    }

    public static Document jsonToDocument(String json){

        ObjectMapper mapper = new ObjectMapper();

        Document document = new Document();
        try{
            mapFromArticleJson = mapper.readValue(json, Map.class);
            document.putAll(mapFromArticleJson);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return document;
    }
}
