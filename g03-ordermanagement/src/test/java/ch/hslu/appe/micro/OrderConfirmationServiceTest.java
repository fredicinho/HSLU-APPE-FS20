package ch.hslu.appe.micro;

import ch.hslu.appe.entities.Order;
import ch.hslu.appe.entities.OrderConfirmation;
import ch.hslu.appe.entities.OrderPosition;
import ch.hslu.appe.entities.State;
import ch.hslu.appe.mongoDB.MongoDbAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class OrderConfirmationServiceTest {
    private MongoDbAdapter adapter = mock(MongoDbAdapter.class);
    private String databaseName = "falseDBName";
    private String collectionName = "falseCollectionName";
    private Date date = new Date();
    private ObjectId id = new ObjectId();
    private Order testOrder = new Order(new ObjectId(), new Date(), State.WAITING_FOR_PRODUCT, "1", Arrays.asList(new OrderPosition("123", 3)));
    private OrderConfirmation testOrderConfirmation = new OrderConfirmation(date, "1", testOrder.getId().toString(), "Hans", "Wurst", testOrder.getOrderPositionList());
    private Document testOrderConfirmationAsDocument = Document.parse(new ObjectMapper().writeValueAsString(testOrderConfirmation));

    public OrderConfirmationServiceTest() throws JsonProcessingException {
    }

    @Test
    public void testConstructor() {
        OrderConfirmationService service = new OrderConfirmationService(this.adapter, OrderConfirmation.class);
        assertEquals(service.getType(), OrderConfirmation.class);
    }

    @Test
    public void testConstructor1() {
        OrderConfirmationService service = new OrderConfirmationService();
        assertEquals(service.getType(), OrderConfirmation.class);
    }

    @Test
    public void testCheckCollectionOnTrue() {
        OrderService service = new OrderService(this.adapter, Order.class);
        when(this.adapter.getDatabaseName()).thenReturn("orderManagement");
        when(this.adapter.getCollectionName()).thenReturn("orderConfirmations");
        service.checkCollection();
        verify(this.adapter, times(1)).getCollectionName();
        verify(this.adapter, times(1)).getDatabaseName();
    }

    @Test
    public void testCheckCollectionOnFalse() {
        this.adapter = mock(MongoDbAdapter.class);
        when(this.adapter.getDatabaseName()).thenReturn(this.databaseName);
        OrderConfirmationService service = new OrderConfirmationService(this.adapter, OrderConfirmation.class);
        service.checkCollection();
        verify(this.adapter, times(1)).changeCollection("orderManagement", "orderConfirmations");
    }

    @Test
    public void testCreateOrderConfirmation() throws JsonProcessingException {
        OrderConfirmationService service = new OrderConfirmationService(this.adapter, OrderConfirmation.class);
        OrderConfirmation result = service.createOrderConfirmation(testOrderConfirmation);
        assertFalse(result.getId().equals(id));
        verify(this.adapter, times(1)).create(any(Document.class));
    }

    @Test
    public void testGetOrderConfirmationByID() throws IOException {
        when(this.adapter.getOneByID(id.toString())).thenReturn(testOrderConfirmationAsDocument);
        OrderConfirmationService service = new OrderConfirmationService(this.adapter, OrderConfirmation.class);
        OrderConfirmation result = service.getOrderConfirmationByID(id.toString());
        assertEquals(result.getArticleList(), testOrderConfirmation.getArticleList());
        assertEquals(result.getCustomerID(), testOrderConfirmation.getCustomerID());
        assertEquals(result.getOrderID(), testOrderConfirmation.getOrderID());
    }
}
