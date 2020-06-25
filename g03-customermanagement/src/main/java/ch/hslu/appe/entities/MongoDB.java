package ch.hslu.appe.entities;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class MongoDB implements Datebases {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDB.class);

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final String databaseName = "customer-management-DB";

    //mongoClient here is a dependency injection provided by micronaut service.
    //network address and port are now defined in the application.yml file
    public MongoDB(final MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        //access the customer-management database
        this.database = this.mongoClient.getDatabase(databaseName);
        LOGGER.info("Created mongo database {}:",  databaseName);
    }

     /**
     * MongoDB collection containing customers.
     * @return the MongoDB collection containing the customer data.
     */
    public MongoDatabase getDatabase() {
        return this.database;
    }
}
