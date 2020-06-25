package ch.hslu.appe.micro;

import ch.hslu.appe.entities.Order;
import ch.hslu.appe.entities.OrderPosition;
import ch.hslu.appe.entities.State;
import ch.hslu.appe.mongoDB.MongoDbAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;


public class OrderServiceTest {
    private MongoDbAdapter adapter = mock(MongoDbAdapter.class);
    private String databaseName = "falseDBName";
    private String collectionName = "falseCollectionName";
    private Date date = new Date();
    private ObjectId id = new ObjectId();
    private Order testOrder = new Order(id, date, State.DELIVERED, "1", Arrays.asList(new OrderPosition("123", 3)));
    private Document testDocument = Document.parse(new ObjectMapper().writeValueAsString(testOrder));

    public OrderServiceTest() throws JsonProcessingException {
    }


    @Test
    public void testConstructor() {
        OrderService service = new OrderService(this.adapter, Order.class);
        assertEquals(service.getType(), Order.class);
    }

    @Test
    public void testConstructor1() {
        OrderService service = new OrderService();
        assertEquals(service.getType(), Order.class);
    }

    @Test
    public void testCheckCollectionOnTrue() {
        OrderService service = new OrderService(this.adapter, Order.class);
        when(this.adapter.getDatabaseName()).thenReturn("orderManagement");
        when(this.adapter.getCollectionName()).thenReturn("orders");
        service.checkCollection();
        verify(this.adapter, times(1)).getCollectionName();
        verify(this.adapter, times(1)).getDatabaseName();
    }

    @Test
    public void testCheckCollectionOnFalse() {
        this.adapter = mock(MongoDbAdapter.class);
        when(this.adapter.getDatabaseName()).thenReturn(this.databaseName);
        OrderService service = new OrderService(this.adapter, Order.class);
        service.checkCollection();
        verify(this.adapter, times(1)).changeCollection("orderManagement", "orders");
    }

    @Test
    public void testCreateOrder() throws IOException {
        when(this.adapter.getDatabaseName()).thenReturn("orderManagement");
        when(this.adapter.getCollectionName()).thenReturn("orders");
        OrderService service = new OrderService(this.adapter, Order.class);
        Order newOrder = service.createOrder(this.testOrder);
        verify(this.adapter, times(1)).create(any(Document.class));
    }

    @Test
    public void testUpdateOrder() throws IOException {
        OrderService service = new OrderService(this.adapter, Order.class);
        service.updateOrder(testOrder);
        verify(this.adapter, times(1)).update(any(Bson.class), any(Document.class));
    }

    @Test
    public void testGetOrderByID() throws IOException {
        when(this.adapter.getOneByID(id.toString())).thenReturn(testDocument);
        OrderService service = new OrderService(this.adapter, Order.class);
        Order result = service.getOrderByID(id.toString());
        assertEquals(result, testOrder);
    }
}
