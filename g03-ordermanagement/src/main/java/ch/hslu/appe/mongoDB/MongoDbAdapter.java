package ch.hslu.appe.mongoDB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.NoSuchElementException;

public class MongoDbAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(MongoDbAdapter.class);
    private MongoDatabase database;
    private String databaseName;
    private String collectionName;
    private MongoCollection collection;
    private MongoDbConfig config;
    private MongoClient mongoClient;
    private static MongoDbAdapter instance;

    private MongoDbAdapter() {
        this.config = new MongoDbConfig();
        this.mongoClient = new MongoClient(new MongoClientURI(this.config.getConnectionAdress()));
        MongoDbAdapter.instance = this;
    }


    public static synchronized MongoDbAdapter getInstance() {
        if (MongoDbAdapter.instance == null) {
            MongoDbAdapter.instance = new MongoDbAdapter();
        }
        return MongoDbAdapter.instance;
    }

    public String getCollectionName() {
        return this.collectionName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }


    /**
     * Changes used database and collection.
     * @param newDatabase the new Database
     * @param newCollection the new Collection
     */
    public void changeCollection(final String newDatabase, final String newCollection) {
        this.database = this.mongoClient.getDatabase(newDatabase);
        this.databaseName = newDatabase;
        this.collection = this.database.getCollection(newCollection);
        this.collectionName = newCollection;
        LOG.info("OrderService, Changed to database: " + database + " and Collection: " + collection);
    }

    /**
     * Creates new Object in the Database.
     * @param obj Object that will be created in the Collection
     *
     */
    public void create(final Document obj) {
        this.collection.insertOne(obj);
        LOG.info("OrderService, New Object (" + obj.toString() + ") created in Collection " + this.collection.toString());
    }


    /**
     * Updates a Object which matches the filter.
     * @param filter Filter
     * @param obj New Object that will be set instead
     * @throws IOException if convert to Document fails or update is not acknowledged
     */
    public void update(final Bson filter, final Document obj) throws IOException {
        Bson update = new Document("$set", obj);
        if (!this.collection.updateOne(filter, update).wasAcknowledged()) {
            throw new IOException("Update of Object was not Acknowledged!");
        }
        LOG.debug("OrderService, Updated one Document which match Filter: " + filter + " with Value: " + obj);
    }

    /**
     * Gets exactly one Obj from the Collection that matches the query.
     * @param id ID of Order
     * @return Gets one Obj that matches with the query.
     * @throws NoSuchElementException Gets thrown if no Obj is found that matches the query.
     */
    public Document getOneByID(final String id) {
        LOG.debug("OrderService, Get one Document which matches ID: " + id);
        try {
            ObjectId obID = new ObjectId(id);
            return getOne(Filters.eq("_id", obID));
        } catch (IllegalArgumentException ex) {
            LOG.warn("OrderService, MongoDbAdapter, getOneByID, ID is not a correct HEX-String.");
            return null;
        }

    }

    /**
     * Method which gets one Object by passed filter.
     * @param filter Filter for searching Element in DB
     * @return Founded element as type "Document" if founded, else throws NoSuchElement-Exception
     */
    public Document getOne(final Bson filter) throws NoSuchElementException {
        LOG.info("OrderService, Get one Document which match Filter: " + filter);
        Document result = (Document) this.collection.find(filter).limit(1).first();
        if (result != null) {
            return result;
        } else {
            throw new NoSuchElementException("Could not find Element with query=" + filter);
        }
    }
}
