package ch.hslu.appe.micro;
import ch.hslu.appe.entities.OrderConfirmation;
import ch.hslu.appe.mongoDB.MongoDbAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.io.IOException;
import java.util.Date;

public class OrderConfirmationService implements ServiceType<OrderConfirmation> {
    private MongoDbAdapter adapter;
    private String databaseName = "orderManagement";
    private String collectionName = "orderConfirmations";
    private Class<OrderConfirmation> type;

    public OrderConfirmationService() {
        this(MongoDbAdapter.getInstance(), OrderConfirmation.class);
    }

    OrderConfirmationService(final MongoDbAdapter adapter, final Class<OrderConfirmation> type) {
        this.adapter = adapter;
        this.type = type;
    }

    /**
     * Changes collection of MongoClient.
     */
    public void changeCollection() {
        this.adapter.changeCollection(databaseName, collectionName);
    }

    /**
     * Checks collection to ensure that the Service uses the correct collection.
     */
    public void checkCollection() {
        if (!(this.adapter.getDatabaseName() == this.databaseName
                && this.adapter.getCollectionName() == this.collectionName)) {
            this.changeCollection();
        }
    }

    /**
     * Gives Type of entity back which this service handles.
     * @return Type of Entity of this Service (OrderConfirmation)
     */
    @Override
    public Class<OrderConfirmation> getType() {
        return this.type;
    }

    /**
     * Creates a new OrderConfirmation by setting new ID and actual Datetime.
     * @param orderConfirmation new OrderConfirmation which will be created.
     * @throws JsonProcessingException throws exception if couldn't convert orderconfirmation
     * @return new Created Order Confirmation
     */
    public OrderConfirmation createOrderConfirmation(final OrderConfirmation orderConfirmation)
            throws JsonProcessingException {
        orderConfirmation.setId(new ObjectId());
        orderConfirmation.setDate(new Date());
        this.checkCollection();
        this.adapter.create(this.convert(orderConfirmation));
        return orderConfirmation;
    }

    /**
     * Searchs in DB an OrderConfirmation by specified ID.
     * @param orderconfirmationID Id of Order to search
     * @return OrderConfirmation which was found.
     * @throws IOException If no Object was founded with this ID in the Database.
     */
    public OrderConfirmation getOrderConfirmationByID(final String orderconfirmationID) throws IOException {
        this.checkCollection();
        Document result = this.adapter.getOneByID(orderconfirmationID);
        if (result != null) {
            return this.convert(result);
        } else {
            throw new IOException("No OrderConfirmation found with this ID");
        }
    }

    /**
     * Searchs in DB an OrderConfirmation by OrderID.
     * @param orderID ID of Order
     * @return OrderConfirmation which was found
     * @throws IOException If order wasnt found.
     */
    public OrderConfirmation getOrderConfirmationByIdOfOrder(final String orderID) throws IOException {
        this.checkCollection();
        Document result = this.adapter.getOne(Filters.eq("orderID", orderID));
        return this.convert(result);
    }
}
