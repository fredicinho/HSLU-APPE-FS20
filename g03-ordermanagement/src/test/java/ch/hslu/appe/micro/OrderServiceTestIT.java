package ch.hslu.appe.micro;

import ch.hslu.appe.entities.Order;
import ch.hslu.appe.entities.OrderConfirmation;
import ch.hslu.appe.entities.OrderPosition;
import ch.hslu.appe.entities.State;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class OrderServiceTestIT {
    private ObjectId id = new ObjectId();
    private Order testOrder = new Order(id, new Date(), State.WAITING_FOR_PRODUCT, "1", Arrays.asList(new OrderPosition("123", 3)));


    @Test
    public void testCreateOrder() throws IOException {
        OrderService service = new OrderService();
        Order order = service.createOrder(testOrder);
        assertEquals(order, service.getOrderByID(order.getId().toString()));
    }

    @Test
    public void testUpdateOrder() throws IOException {
        OrderService service = new OrderService();
        Order order = service.createOrder(testOrder);
        order.setState(State.DELIVERED);
        service.updateOrder(order);
        assertEquals(State.DELIVERED, service.getOrderByID(order.getId().toString()).getState());
    }

    @Test
    public void testUpdateOrder2() throws IOException {
        OrderService service = new OrderService();
        Order order = service.createOrder(testOrder);
        order.setCustomerID("123456");
        service.updateOrder(order);
        assertEquals("123456", service.getOrderByID(order.getId().toString()).getCustomerID());
    }

}
