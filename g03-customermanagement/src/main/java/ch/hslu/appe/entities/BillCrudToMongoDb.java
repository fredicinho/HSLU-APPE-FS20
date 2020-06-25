package ch.hslu.appe.entities;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bson.types.ObjectId;
import javax.inject.Singleton;

@Singleton
public class BillCrudToMongoDb implements BillCrudOperations {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillCrudToMongoDb.class);
    private final MongoDB db;
    private final String collectionName = "bills";
    private MongoCollection<Bill> collection;

    public BillCrudToMongoDb(final MongoDB mongoDB) {
        this.db = mongoDB;
        this.collection = this.db.getDatabase().getCollection(collectionName, Bill.class);
    }

    /**
     * @param newBill a bill object.
     * @return a bill object.
     */
    @Override
    public Bill create(final Bill newBill) {
        this.collection.insertOne(newBill);
        LOGGER.info("Bill created with id: {}.", newBill.getId());
        LOGGER.info("Bill looks like that: {}.", newBill);
        return newBill;
    }

    /**
     * @param id the bill id.
     * @return a bill object.
     */
    @Override
    public Bill getBillByID(final String id)  {
        Bill foundedBillAsDocument = collection.find(Filters.eq("_id", new ObjectId(id))).limit(1).first();
        LOGGER.info("Bill founded with id: {}.", id);
        return foundedBillAsDocument;
    }


}
