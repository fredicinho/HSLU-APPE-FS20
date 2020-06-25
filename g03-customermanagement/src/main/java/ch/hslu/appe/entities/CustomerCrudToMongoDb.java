package ch.hslu.appe.entities;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.inject.Singleton;

import static com.mongodb.client.model.Filters.eq;

@Singleton
//mongoClient here is a dependency injection provided by micronaut service.
public class CustomerCrudToMongoDb implements CustomerCrudOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerCrudToMongoDb.class);
    private final MongoDB db;
    private static final String COLLECTION_NAME = "customers";
    private MongoCollection<Customer> collection;

    public CustomerCrudToMongoDb(final MongoDB mongodb) {

        this.db = mongodb;
        this.collection = this.db.getDatabase().getCollection(COLLECTION_NAME, Customer.class);
    }

    /**
     * Updates an existing customer. Considered existing if we receive an ID.
     * Inserts a new customer. Considered new if we do not receive an ID.
     * @param customer new customer.
     * @return Customer object.
     */
    @Override
    public Customer upsert(final Customer customer) {
        UpdateResult result;
        String id = customer.getUuid();

        //if the id is not null, it is an update of an existing customer, not an insert.
        if (id != null) {
            // Check if UUID is valid
            try {
                UUID.fromString(id);
            } catch (IllegalArgumentException ex) {
                LOGGER.warn("Invalid customer ID. Could not update customer with Id: {}.", id);
                return null;
            }
            result = this.collection.replaceOne(eq("uuid", customer.getUuid()), customer);
            LOGGER.debug("getCollection.replaceOne() result: {}, ack: {}.", result, result.wasAcknowledged());
            if (result.getMatchedCount() < 1) {
                LOGGER.warn("Customer ID not found in database. Could not update customer with Id: {}.", id);
                return null;
            } else {
                LOGGER.info("Customer already exists in database. "
                        + "Successfully updated record for customer: {}.", id);
                return customer;
            }
        }
        //if we get here, we did not receive an id and a new customer needs to be created in the database.
        customer.setAdmonitionLevel(AdmonitionLevel.NOTHING);
        return insert(customer);
    }

    /**
     * Creates a new customer with a new id in the database.
     * @param customer the customer we receive, without id.
     * @return the customer returned from the database after inserting it. Including the uuid that was created here.
     */
    private Customer insert(final Customer customer) {
        customer.setUuid(UUID.randomUUID().toString());
        InsertOneResult result = collection.insertOne(customer);
        // Note: insertOne adds ID if missing. It changes the customer object itself.
        // By reference. This is why we can use the customer again here.
        String id = result.getInsertedId().asObjectId().getValue().toString();
        LOGGER.info("Created a new customer in database with _id: {}, UUID: {}.", id, customer.getUuid());
        return customer;
    }

    /**
     * @param uuid of the customer.
     * @return Returns true if at least one record with this ID was deleted. False if no such record was found.
     */
    @Override
    public boolean delete(final String uuid) {
        long deleted = collection.deleteOne(eq("uuid", uuid)).getDeletedCount();
        return (deleted > 0);
    }

    /**
     * @param uuid customer ID as a String.
     * @return The customer we found. Returns null if no customer is found matching provided ID.
     */
    @Override
    public Customer getOneById(final String uuid) {
        FindIterable<Customer> iter = collection.find(eq("uuid", uuid));
        Customer customer = iter.first();
        LOGGER.debug("getOneById found customer: {}.", customer);
        return customer;
    }

    /**
     * Returns a list of customers by last name.
     * @param lastName of this customer.
     * @return List of customers by that last name. Returns an empty list if no match is found.
     */
    @Override
    public Collection<Customer> findByLastName(final String lastName) {
        Collection<Customer> customers =
                collection.find(eq("lastName", lastName)).into(new ArrayList<Customer>());
        for (Customer customer: customers) {
            LOGGER.info("findByLastName() found customer matching last name {}: {} {},",
                    lastName, customer.getFirstName(), customer.getLastName());
        }
        return customers;
    }

    /**
     * Returns a list of all customers stored in the collection.
     * @return list of customers. Returns empty list if collection is empty.
     */
    @Override
    public Collection<Customer> getAll() {
        Collection<Customer> customers = collection.find().into(new ArrayList<Customer>());
        for (Customer customer: customers) {
            LOGGER.info("getAll() found the following customer: {}.", customer);
        }
        return customers;
    }

}
