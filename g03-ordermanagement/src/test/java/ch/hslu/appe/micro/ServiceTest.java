package ch.hslu.appe.micro;

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.entities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ServiceTest {
    private OrderService orderService = mock(OrderService.class);
    private OrderConfirmationService orderConfirmationService = mock(OrderConfirmationService.class);
    private BusConnector bus = mock(BusConnector.class);
    private String testOrderAsJson = "{\"customerID\":\"bcff3ee2-f9ff-4757-a8e7-07ff4b65fd7d\",\"orderPositionList\":[{\"articleID\":\"765f42cc-c319-41c0-9af6-f7c78cfdff7d\",\"count\":3}]}";
    private ObjectId id = new ObjectId();
    private Order testOrder = new Order(id, new Date(), State.WAITING_FOR_PRODUCT, "1", Arrays.asList(new OrderPosition("123", 3)));
    private String _testOrderAsJson = new ObjectMapper().writeValueAsString(testOrder);
    private String orderAvailabilityRequest = "{\"orderPositionList\":[{\"articleID\":\"765f42cc-c319-41c0-9af6-f7c78cfdff7d\",\"count\":3,\"available\":false,\"price\":0.0}],\"id\":\"5eb2a8d8b9a90451093b8920\"}";
    private String customerAsJson = "{\"customer\": {\"uuid\": \"b129eb5a-011f-4ee7-9e45-f17b32bbda1a\",\"first_name\": \"Walter\",\"last_name\": \"White\", \"street\": \"Wyoming Bldv\", \"number\": \"1501\",\"zip\": \"8002\",\"city\": \"Albuquerque\",\"email\": \"walter.white@breakingbad.com\",\"phone\": \"+41787894502\", \"admonitionLevel\":\"NOTHING\"},\"success\": true}";
    private String customerAsJsonWithAdmonition = "{\"customer\": {\"uuid\": \"b129eb5a-011f-4ee7-9e45-f17b32bbda1a\",\"first_name\": \"Walter\",\"last_name\": \"White\", \"street\": \"Wyoming Bldv\", \"number\": \"1501\",\"zip\": \"8002\",\"city\": \"Albuquerque\",\"email\": \"walter.white@breakingbad.com\",\"phone\": \"+41787894502\", \"admonitionLevel\":\"FIRST_LEVEL\"},\"success\": true}";
    private OrderConfirmation testOrderConfirmation = new OrderConfirmation(new Date(), "1", testOrder.getId().toString(), "Hans", "Wurst", testOrder.getOrderPositionList());
    private String testOrderConfirmationAsJson = new ObjectMapper().writeValueAsString(testOrderConfirmation);
    private OrderAvailabilityRequest request = new OrderAvailabilityRequest(testOrder.getOrderPositionList(), testOrder.getId().toString());
    private String requestAsJson = new ObjectMapper().writeValueAsString(request);
    public ServiceTest() throws JsonProcessingException {
    }

    @Test
    public void testOnGetOrderByID() throws IOException, TimeoutException {
        when(this.orderService.getOrderByID("1")).thenReturn(testOrder);
        Response response = new Response(Status.OK, "Order founded!", _testOrderAsJson);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onGetOrderByID("", "replyTo", "1", "correlationId");
        verify(this.bus, times(1)).talkAsync("", "replyTo", new ObjectMapper().writeValueAsString(response));
    }

    @Test
    public void testOnGetOrderByIDWithJsonProcessingException() throws IOException, TimeoutException {
        when(this.orderService.getOrderByID("1")).thenThrow(JsonProcessingException.class);
        Response response = new Response(Status.ERROR, "There was a Problem reading the message!", "");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onGetOrderByID("", "replyTo", "1", "correlationId");
        verify(this.bus, times(1)).talkAsync("", "replyTo", jsonResponse);
    }

    @Test
    public void testOnGetOrderByIDWithIOException() throws IOException, TimeoutException {
        when(this.orderService.getOrderByID("1")).thenThrow(IOException.class);
        Response response = new Response(Status.PRODUCTS_NOT_AVAILABLE, "Couldn't find Order in DB with this ID.", "");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onGetOrderByID("", "replyTo", "1", "correlationId");
        verify(this.bus, times(1)).talkAsync("", "replyTo", jsonResponse);
    }

    @Test
    public void testOnNewOrderReceived() throws IOException, InterruptedException, TimeoutException {
        Order order = new ObjectMapper().readValue(testOrderAsJson, Order.class);
        ObjectId id = new ObjectId();
        order.setId(id);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(orderService.getOrderByID(any(String.class))).thenReturn(testOrder);
        doNothing().when(orderService).updateOrder(any(Order.class));
        when(bus.talkSync(any(String.class), any(String.class), any(String.class))).thenReturn(orderAvailabilityRequest);
        when(bus.talkSync("g03", "customer.getOneById", "1")).thenReturn(customerAsJson);
        when(orderConfirmationService.createOrderConfirmation(any(OrderConfirmation.class))).thenReturn(testOrderConfirmation);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onNewOrderReceived("g03", "replyTo", testOrderAsJson, "correlationID");
        String response = new ObjectMapper().writeValueAsString(new Response(Status.OK, "Order created successfully! Here is your orderconfirmation: ", new ObjectMapper().writeValueAsString(testOrderConfirmation)));
        System.out.println(response);
        verify(bus, times(2)).talkAsync(any(String.class), any(String.class), any(String.class));
    }

    @Test
    public void testOnNewOrderReceivedWithCustomerWithAdmonition() throws IOException, InterruptedException, TimeoutException {
        Order order = new ObjectMapper().readValue(testOrderAsJson, Order.class);
        ObjectId id = new ObjectId();
        order.setId(id);
        when(orderService.createOrder(any(Order.class))).thenReturn(order);
        when(orderService.getOrderByID(any(String.class))).thenReturn(testOrder);
        doNothing().when(orderService).updateOrder(any(Order.class));
        when(bus.talkSync(any(String.class), any(String.class), any(String.class))).thenReturn(orderAvailabilityRequest);
        when(bus.talkSync("g03", "customer.getOneById", "1")).thenReturn(customerAsJsonWithAdmonition);
        Response response = new Response(Status.ERROR, "Customer is not allowed to make new Orders. Check AdmonitionLevel.", "");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);

        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onNewOrderReceived("", "replyTo", testOrderAsJson, "correlationID");
        verify(bus, times(1)).talkAsync("", "replyTo", jsonResponse);


    }

    @Test
    public void testOnNewOrderReceivedCatchingIOException() throws IOException, TimeoutException, InterruptedException {
        when(orderService.createOrder(any(Order.class))).thenThrow(IOException.class);
        Response response = new Response(Status.ERROR, "Order couldn't be created.", "");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onNewOrderReceived("g03", "replyTo", testOrderAsJson, "correlationID");
        verify(this.bus, times(1)).talkAsync("g03", "replyTo", jsonResponse);
    }

    @Test
    public void testOnGetOrderConfirmationbyID() throws IOException, TimeoutException {
        when(this.orderConfirmationService.getOrderConfirmationByID("1")).thenReturn(testOrderConfirmation);
        Response response = new Response(Status.OK, "Order Confirmation founded!", testOrderConfirmationAsJson);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onGetOrderConfirmationByID("", "replyTo", "1", "correlationId");
        verify(this.bus, times(1)).talkAsync("", "replyTo", new ObjectMapper().writeValueAsString(response));
    }

    @Test
    public void testOnGetOrderConfirmationbyIDWithJsonProcessingException() throws IOException, TimeoutException {
        when(this.orderConfirmationService.getOrderConfirmationByID("1")).thenThrow(JsonProcessingException.class);
        Response response = new Response(Status.ERROR, "There was a Problem reading the message!", "");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onGetOrderConfirmationByID("", "replyTo", "1", "correlationId");
        verify(this.bus, times(1)).talkAsync("", "replyTo", jsonResponse);
    }

    @Test
    public void testOnGetOrderConfirmationbyIDWithIOException() throws IOException, TimeoutException {
        when(this.orderConfirmationService.getOrderConfirmationByID("1")).thenThrow(IOException.class);
        Response response = new Response(Status.PRODUCTS_NOT_AVAILABLE, "Couldn't find OrderConfirmation in DB with this ID.", "");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onGetOrderConfirmationByID("", "replyTo", "1", "correlationId");
        verify(this.bus, times(1)).talkAsync("", "replyTo", jsonResponse);
    }

    @Test
    public void testOnAvailabilityResponseFalse() throws IOException, TimeoutException {
        when(this.orderService.getOrderByID(this.id.toString())).thenReturn(testOrder);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        service.onAvailabilityCheckResponse("", "replyTo", requestAsJson, "correlationid");
        System.out.println(requestAsJson);
        verify(this.orderService, times(1)).updateOrder(testOrder);
    }

    @Test
    public void testOnAvailabilityResponseTrue() throws IOException, TimeoutException {
        when(this.orderService.getOrderByID(this.id.toString())).thenReturn(testOrder);
        when(this.orderConfirmationService.getOrderConfirmationByIdOfOrder(this.id.toString())).thenReturn(testOrderConfirmation);
        Service service = new Service(this.bus, this.orderService, this.orderConfirmationService);
        this.request.getOrderPositionList().stream().forEach(position -> {
            position.setAvailable(true);
        });
        String newRequest = new ObjectMapper().writeValueAsString(this.request);
        service.onAvailabilityCheckResponse("", "replyTo", newRequest, "correlationid");
        verify(this.orderService, times(1)).updateOrder(testOrder);
        assertEquals(testOrder.getState(), State.DELIVERED);
    }


}
