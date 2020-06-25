package ch.hslu.appe.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class OrderTest {

    @Test
    public void testConstructor1() {
        Order emptyTestOrder = new Order();
        assertTrue(emptyTestOrder instanceof Order);
    }

    @Test
    public void testConstructor2() {
        Order order = new Order(new ObjectId(),new Date(), State.STATE_NOT_AVAILABLE, "3242", Arrays.asList(new OrderPosition("1", 1)));
        assertTrue(order instanceof Order);
    }

    @Test
    public void testID() {
        ObjectId id = new ObjectId();
        Order order = new Order();
        order.setId(id);
        assertEquals(id, order.getId());
    }

    @Test
    public void testState() {
        Order order = new Order();
        order.setState(State.CANCELLED);
        assertEquals(State.CANCELLED, order.getState());
    }

    @Test
    public void testDate() {
        Date date = new Date();
        Order order = new Order(new ObjectId(),date, State.STATE_NOT_AVAILABLE, "3242", Arrays.asList(new OrderPosition("1", 1)));
        assertEquals(date, order.getDate());
    }

    @Test
    public void testCustomerID() {
        Order order = new Order();
        order.setCustomerID("1");
        assertEquals( "1", order.getCustomerID());
    }


    @Test
    public void testOrderPositions() {
        Order order = new Order(new ObjectId(),new Date(), State.STATE_NOT_AVAILABLE, "3242", Arrays.asList(new OrderPosition("1", 1)));
        assertEquals(order.getOrderPositionList(), Arrays.asList(new OrderPosition("1", 1)));
    }

    @Test
    public void testSerializeObjectID() throws JsonProcessingException {
        Date date = new Date();
        ObjectId id = new ObjectId();
        Order order = new Order(id,date, State.STATE_NOT_AVAILABLE, "3242", Arrays.asList(new OrderPosition("1", 1)));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.out.println(json);
        Order newOrder = mapper.readValue(json, Order.class);
        assertEquals(id, newOrder.getId());
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        Date date = new Date();
        ObjectId id = new ObjectId();
        Order order = new Order(id,date, State.STATE_NOT_AVAILABLE, "3242", Arrays.asList(new OrderPosition("1", 1)));
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.out.println(json);
        Order newOrder = mapper.readValue(json, Order.class);
        assertEquals(newOrder.getId(), id);
        assertEquals("3242", newOrder.getCustomerID());
        assertEquals(State.STATE_NOT_AVAILABLE, newOrder.getState());
        assertEquals(Arrays.asList(new OrderPosition("1", 1)), newOrder.getOrderPositionList());
    }


}
