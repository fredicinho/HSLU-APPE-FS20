package ch.hslu.appe.micro;

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageReceiver;
import ch.hslu.appe.bus.RabbitMqConfig;
import ch.hslu.appe.entities.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

public class Service implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(Service.class);
    private static final String ROUTE_ORDER_NEWORDER = "order.neworder";
    private static final String ROUTE_STORAGE_ORDERREQUEST = "storage.orderrequest";
    private static final String ROUTE_ORDER_GETONE = "order.getone";
    private static final String ROUTE_ORDER_AVAILABILITY = "order.availabilityResponse";
    private static final String ROUTE_CUSTOMER_GETONE_QUEUE = "customer.getOneById";
    private static final String ROUTE_ORDER_GETONECONFIRMATION = "order.getoneconfirmation";
    private static final String ROUTE_STORAGE_GETPRICE = "storage.getpriceoforderlist";
    private static final String ROUTE_CUSTOMER_CREATEBILL = "customer.createBill";

    private final String exchangeName;
    private final BusConnector bus;
    private final OrderService orderService;
    private final OrderConfirmationService orderConfirmationService;

    /**
     * @throws IOException IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    Service() throws IOException, TimeoutException {
        this(new BusConnector(), new OrderService(), new OrderConfirmationService());
    }

    Service(final BusConnector bus, final OrderService orderService,
            final OrderConfirmationService orderConfirmationService)
            throws IOException, TimeoutException {
        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = bus;
        this.bus.connect();
        this.orderService = orderService;
        this.orderConfirmationService = orderConfirmationService;
        this.registerListener(this.ROUTE_ORDER_NEWORDER, this::onNewOrderReceived);
        this.registerListener(this.ROUTE_ORDER_GETONE, this::onGetOrderByID);
        this.registerListener(this.ROUTE_ORDER_AVAILABILITY, this::onAvailabilityCheckResponse);
        this.registerListener(this.ROUTE_ORDER_GETONECONFIRMATION, this::onGetOrderConfirmationByID);
    }

    /**
     * Method which creates a Queue with a defined route.
     * @param route Listening on the queue with this routename.
     * @param receiver Receiver which implements the work he has to do with the received message.
     * @throws IOException
     */
    private void registerListener(final String route, final MessageReceiver receiver) throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", route);
        bus.listenFor(exchangeName, "Order | " + route, route, receiver);
    }

    /**
     * Message Receiver which handles Requests of Orders by ID.
     * @param route Route where message was received
     * @param replyTo Queue where to reply to
     * @param message Message which contains Data
     * @param correlationId CorrelationID
     * @throws IOException If communication with BUS didnt work
     */
    public void onGetOrderByID(final String route, final String replyTo,
                               final String message, final String correlationId) throws IOException {
        // log event
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Begin message processing", threadName);
        LOG.debug("Received message with routing [{}]", route);
        LOG.debug("Received Message: " + message);
        LOG.debug("Received Reply-TO" + replyTo);

        // unpack received message data
        ObjectMapper mapper = new ObjectMapper();
        try {
            Order order = this.orderService.getOrderByID(message);
            String responseMessage = mapper.writeValueAsString(order);
            Response response = new Response(Status.OK, "Order founded!", responseMessage);
            String jsonResponse = mapper.writeValueAsString(response);
            LOG.info("OrderService, GetOrderByID, Response: " + jsonResponse);
            this.bus.talkAsync("", replyTo, jsonResponse);
        } catch (JsonProcessingException e) {
            LOG.debug("Couldn't read Message. Return Response with Errormessage.");
            e.printStackTrace();
            Response response = new Response(Status.ERROR, "There was a Problem reading the message!", "");
            String jsonResponse = mapper.writeValueAsString(response);
            LOG.info("OrderService, GetOrderByID, Response: " + jsonResponse);
            this.bus.talkAsync("", replyTo, jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.debug("Couldn't find Order in DB. Return Response with Message.");
            e.printStackTrace();
            Response response = new Response(Status.PRODUCTS_NOT_AVAILABLE,
                    "Couldn't find Order in DB with this ID.", "");
            String jsonResponse = mapper.writeValueAsString(response);
            LOG.info("OrderService, GetOrderByID, Response: " + jsonResponse);
            this.bus.talkAsync("", replyTo, jsonResponse);
        }
    }

    /**
     * Message Reiceiver which handles new created Orders. Persist new Order,
     * creates Orderconfirmation sends this to REST back.
     * @param route Route where message was received
     * @param replyTo Queue where to reply to
     * @param message Message which contains data
     * @param correlationId CorrelationID
     * @throws IOException Throws Exception if message couldn't be readed
     */
    public void onNewOrderReceived(final String route, final String replyTo, final String message,
                                   final String correlationId) throws IOException {
        // log event
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Begin message processing", threadName);
        LOG.debug("Received message with routing [{}]", route);
        LOG.debug("Received Message: " + message);
        LOG.debug("Received Reply-TO" + replyTo);

        // unpack received message data
        ObjectMapper mapper = new ObjectMapper();
        try {
            Order receivedOrder = mapper.readValue(message, Order.class);
            Order persistedOrder = this.orderService.createOrder(receivedOrder);
            Order updateOrder = this.getPriceOfProductsInOrder(persistedOrder);
            String customerAsJson = this.bus.talkSync(this.exchangeName,
                    this.ROUTE_CUSTOMER_GETONE_QUEUE, updateOrder.getCustomerID());
            LOG.info("OrderService, creating New Order, Received customer of customerservice: " + customerAsJson);
            JsonNode customerAsJsonNode = mapper.readTree(customerAsJson);
            JsonNode customerNode = customerAsJsonNode.path("customer");
            if (!customerNode.get("admonitionLevel").asText().equals("NOTHING")) {
                LOG.info("OrderService, creating New Order, Customers admonitionLevel is not NOTHING. "
                        + "Customer is not allowed to make new Orders.");
                Response response = new Response(Status.ERROR,
                        "Customer is not allowed to make new Orders. Check AdmonitionLevel.", "");
                String jsonResponse = mapper.writeValueAsString(response);
                this.bus.talkAsync("", replyTo, jsonResponse);
                return;
            }
            String firstName = customerNode.get("first_name").asText();
            String lastName = customerNode.get("last_name").asText();
            OrderConfirmation orderConfirmation = new OrderConfirmation(new Date(),
                    updateOrder.getCustomerID(), updateOrder.getId().toString(), firstName,
                    lastName, updateOrder.getOrderPositionList());
            OrderConfirmation persistedOrderConfirmation =
                    this.orderConfirmationService.createOrderConfirmation(orderConfirmation);
            Response response = new Response(Status.OK, "Order created successfully! Here is your Orderconfirmation: ",
                    mapper.writeValueAsString(persistedOrderConfirmation));
            String jsonResponse = mapper.writeValueAsString(response);
            LOG.info("OrderService, creating new Order, Response with OrderConfirmation: " + jsonResponse);
            this.bus.talkAsync("", replyTo, jsonResponse);
            this.checkAvailabilityOfOrderPositions(updateOrder);
        } catch (IOException ex) {
            ex.printStackTrace();
            LOG.debug("Couldn't create the Order. Sending ErrorMessage to REST!");
            Response response = new Response(Status.ERROR, "Order couldn't be created.", "");
            String jsonResponse = mapper.writeValueAsString(response);
            LOG.error("OrderService, creating new Order, Order couldn't be created.");
            this.bus.talkAsync(this.exchangeName, replyTo, jsonResponse);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOG.error("OrderService, creating new Order, Order couldn't be created because "
                    + "didnt receive answer of Customerservice.");
            Response response = new Response(Status.ERROR,
                    "Order couldn't be created because of problem With customer", "");
            String jsonResponse = mapper.writeValueAsString(response);
            this.bus.talkAsync(this.exchangeName, replyTo, jsonResponse);
        }
    }

    /**
     * Gets Prices of given Order from the localwarehouse (Synchronous Request).
     * @param order Order with list of articles.
     * @return Processed order where articles contains prices.
     * @throws IOException If mapper couldnt parse ojects
     * @throws InterruptedException If there where problems with Message-Transfer
     */
    public Order getPriceOfProductsInOrder(final Order order) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        OrderAvailabilityRequest orderRequest = new OrderAvailabilityRequest(order.getOrderPositionList(),
                order.getId().toString());
        orderRequest.getOrderPositionList().stream().forEach(position -> {
            position.setAvailable(false);
        });
        String request = mapper.writeValueAsString(orderRequest);
        LOG.info("OrderService, get Price Of Articles from Localwarehouse, Request: ");
        String responseOfStorage = this.bus.talkSync(this.exchangeName, this.ROUTE_STORAGE_GETPRICE, request);
        LOG.info("OrderService, get Price of Articles from Localwarehouse, Received response: " + responseOfStorage);
        OrderAvailabilityRequest processedOrder = mapper.readValue(responseOfStorage, OrderAvailabilityRequest.class);
        Order orderOfDB = this.orderService.getOrderByID(processedOrder.getId());
        orderOfDB.setOrderPositionList(processedOrder.getOrderPositionList());
        this.orderService.updateOrder(orderOfDB);
        return orderOfDB;
    }


    /**
     * Message Receiver which responds with the founded Orderconfirmation.
     * @param route Route where message was received
     * @param replyTo Name of routename where to respons (Normaly REST)
     * @param message Contains orderConfirmationID
     * @param correlationId CorrelationID, not used here
     * @throws IOException If Problems with sending messages
     */
    public void onGetOrderConfirmationByID(final String route, final String replyTo, final String message,
                                           final String correlationId) throws IOException {
        // log event
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Begin message processing", threadName);
        LOG.debug("Received message with routing [{}]", route);
        LOG.debug("Received Message: " + message);
        LOG.debug("Received Reply-TO" + replyTo);

        // unpack received message data
        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderConfirmation orderConfirmation = this.orderConfirmationService.getOrderConfirmationByID(message);
            String responseMessage = mapper.writeValueAsString(orderConfirmation);
            Response response = new Response(Status.OK, "Order Confirmation founded!", responseMessage);
            String jsonResponse = mapper.writeValueAsString(response);
            LOG.info("OrderService, Get OrderConfirmation By ID, Found Order. Sending Response: " + jsonResponse);
            this.bus.talkAsync("", replyTo, jsonResponse);
        } catch (JsonProcessingException e) {
            LOG.debug("Couldn't read Message. Return Response with Errormessage.");
            e.printStackTrace();
            Response response = new Response(Status.ERROR, "There was a Problem reading the message!", "");
            String jsonResponse = mapper.writeValueAsString(response);
            LOG.info("OrderService, Get OrderConfirmation By ID, Couldn' read received message. "
                    + "Sending Response with error: " + jsonResponse);
            this.bus.talkAsync("", replyTo, jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
            Response response = new Response(Status.PRODUCTS_NOT_AVAILABLE,
                    "Couldn't find OrderConfirmation in DB with this ID.", "");
            String jsonResponse = mapper.writeValueAsString(response);
            LOG.info("OrderService, Get OrderConfirmation By ID, Order doesnt exist. Response: " + jsonResponse);
            this.bus.talkAsync("", replyTo, jsonResponse);
        }
    }

    /**
     * Checks received Answers by availability of Articles. Makes an Update on
     * the received Order and if all Articles are available,
     * the state of the order is settet to "Delivered" and bill will be created on customerservice.
     * @param route Route where it comes from.
     * @param replyTo Not used in here.
     * @param message OrderAvailabilityRequest as JSON
     * @param correlationId Not used in here.
     */
    public void onAvailabilityCheckResponse(final String route, final String replyTo,
                                            final String message, final String correlationId) {
        // log event
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Begin message processing", threadName);
        LOG.debug("Received message with routing [{}]", route);
        LOG.debug("Received Message: " + message);
        LOG.debug("Received Reply-TO" + replyTo);

        // unpack received message data
        ObjectMapper mapper = new ObjectMapper();
        try {
            LOG.info("OrderService, AvailabilityCheckResponse, Received message: "
            + message);
            OrderAvailabilityRequest receivedOrder = mapper.readValue(message, OrderAvailabilityRequest.class);
            Order order = this.orderService.getOrderByID(receivedOrder.getId());
            order.setOrderPositionList(receivedOrder.getOrderPositionList());
            boolean allProductsAvailable = true;
            for (OrderPosition position : order.getOrderPositionList()) {
                if (!(position.getAvailable())) {
                    allProductsAvailable = false;
                }
            }
            if (allProductsAvailable) {
                LOG.info("OrderService, AvailabilityCheckResponse, All Products are available! Will send"
                        + " all Articles now to customer and create Bill.");
                order.setState(State.DELIVERED);
                this.orderService.updateOrder(order);
                this.createBill(order);
            } else {
                LOG.info("OrderService, AvailabilityCheckResponse, Not all Products are available. Have "
                        + "to wait till products arrive in storage.");
                this.orderService.updateOrder(order);
                // Have to wait till Articles are available in Storage!
            }
        } catch (JsonProcessingException e) {
            LOG.info("OrderService, AvailabilityCheckResponse, Couldn't create the Order of received Message from Storage!");
            e.printStackTrace();
        } catch (IOException ex) {
            LOG.info("OrderService, AvailabilityCheckResponse, Couldn't update Object in the Database");
            ex.printStackTrace();
        }

    }

    /**
     * Creates a Bill and sends message to Customerservice.
     * @param order Order which contains information for the bill.
     */
    public void createBill(final Order order) {
        OrderConfirmation orderConfirmation = null;
        try {
            orderConfirmation = this.orderConfirmationService.
                    getOrderConfirmationByIdOfOrder(order.getId().toString());
        } catch (IOException e) {
            LOG.error("OrderSerivce, Create Bill, Couldnt get OrderConfirmation by ID.");
            e.printStackTrace();
            return;
        }
        Bill bill = new Bill(orderConfirmation.getOrderID(), orderConfirmation.getCustomerID(),
                orderConfirmation.getArticleList(), orderConfirmation.getWholePrice());
        try {
            String billAsJson = new ObjectMapper().writeValueAsString(bill);
            this.bus.talkAsync(this.exchangeName, this.ROUTE_CUSTOMER_CREATEBILL, billAsJson);
        } catch (IOException e) {
            LOG.error("OrderService, Create Bill, Bill couldnt be sended to Customer-Service.");
            e.printStackTrace();
        }
    }

    /**
     * Method to check on Localwarehouse if Products of an Order are available and if so, they get reserved.
     * @param order order to check available products
     */
    public void checkAvailabilityOfOrderPositions(final Order order) {
        ObjectMapper mapper = new ObjectMapper();
        OrderAvailabilityRequest request = new OrderAvailabilityRequest(order.getOrderPositionList(),
                order.getId().toString());
        String message = null;
        try {
            message = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            LOG.error("OrderService, Check Availability Of Articles, Couldn't write JSON from Order!");
            e.printStackTrace();
            return;
        }
        try {
            this.bus.talkAsync(this.exchangeName, this.ROUTE_STORAGE_ORDERREQUEST, message);
        } catch (IOException e) {
            LOG.error("OrderService, Check Availability of Articles, Couldn't send message to Storage-Service!");
            e.printStackTrace();
        }
    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
