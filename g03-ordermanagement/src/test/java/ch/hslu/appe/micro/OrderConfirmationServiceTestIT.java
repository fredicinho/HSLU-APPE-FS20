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

public class OrderConfirmationServiceTestIT {
    private ObjectId id = new ObjectId();
    private Order testOrder = new Order(id, new Date(), State.WAITING_FOR_PRODUCT, "1", Arrays.asList(new OrderPosition("123", 3)));
    private OrderConfirmation testOrderConfirmation = new OrderConfirmation(new Date(), "1", testOrder.getId().toString(), "Hans", "Wurst", testOrder.getOrderPositionList());


    @Test
    public void testCreateOrderConfirmation() throws IOException {
        OrderConfirmationService service = new OrderConfirmationService();
        OrderConfirmation confirmation = service.createOrderConfirmation(testOrderConfirmation);
        assertEquals(confirmation.getCustomerID(), service.getOrderConfirmationByID(confirmation.getId().toString()).getCustomerID());
    }


    @Test
    public void testGetOrderConfirmationByIDofOrder() throws IOException {
        OrderConfirmationService service = new OrderConfirmationService();
        OrderConfirmation confirmation = service.createOrderConfirmation(testOrderConfirmation);
        assertEquals(confirmation.getCustomerID(), service.getOrderConfirmationByIdOfOrder(confirmation.getOrderID()).getCustomerID());
    }
}
