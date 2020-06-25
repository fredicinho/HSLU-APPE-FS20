package ch.hslu.appe.micro;
import ch.hslu.appe.entities.Order;
import ch.hslu.appe.entities.State;
import ch.hslu.appe.mongoDB.MongoDbAdapter;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Date;

public class OrderService implements ServiceType<Order> {
    private MongoDbAdapter adapter;
    private String databaseName = "orderManagement";
    private String collectionName = "orders";
    private Class<Order> type;

    public OrderService() {
        this(MongoDbAdapter.getInstance(), Order.class);
    }

    OrderService(final MongoDbAdapter adapter, final Class<Order> type) {
        this.adapter = adapter;
        this.type = type;
    }

    /**
     * Changes collection of the mongoclient.
     */
    public void changeCollection() {
        this.adapter.changeCollection(databaseName, collectionName);
    }

    /**
     * Checks collection to ensure this service is using the right collection on the mongoclient.
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
    public Class<Order> getType() {
        return this.type;
    }


    /**
     * Creates a new Order by seting status, new ID and actual Datetime.
     * @param newOrder new Order which will be created.
     * @throws IOException throws exception if couldn't convert order
     * @return Order new Created Order
     */
    public Order createOrder(final Order newOrder) throws IOException {
        newOrder.setState(State.WAITING_FOR_PRODUCT);
        newOrder.setId(new ObjectId());
        newOrder.setDate(new Date());
        this.checkCollection();
        this.adapter.create(this.convert(newOrder));
        return newOrder;
    }

    /**
     * Updates an Order in the Database.
     * @param updatedOrder Updated Order
     * @throws IOException If convertion failed oder Update was not acknowledged.
     */
    public void updateOrder(final Order updatedOrder) throws IOException {
        this.checkCollection();
        this.adapter.update(Filters.eq("_id", updatedOrder.getId()), this.convert(updatedOrder));
    }

    /**
     * Searchs in DB an Order by specified ID.
     * @param orderID Id of Order to search
     * @return Order which was found.
     * @throws IOException If no Object was founded with this ID in the Database.
     */
    public Order getOrderByID(final String orderID) throws IOException {
        this.checkCollection();
        Document result = this.adapter.getOneByID(orderID);
        if (result != null) {
            return this.convert(result);
        } else {
            throw new IOException("No Order found with this ID");
        }
    }


}
