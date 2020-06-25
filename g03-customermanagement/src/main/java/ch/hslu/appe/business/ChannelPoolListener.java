package ch.hslu.appe.business;

import com.rabbitmq.client.Channel;
import io.micronaut.configuration.rabbitmq.connect.ChannelInitializer;

import javax.inject.Singleton;
import java.io.IOException;


//@Singleton makes this a bean. Bean is a standard. Requirements:
    //1. All properties private (use getters/setters)
    //2. A public no-argument constructor
    //3  Implements Serializable.

//The class is declared as a singleton so it will be registered with the context
@Singleton
public class ChannelPoolListener extends ChannelInitializer {
    //name of the rabbitMQ queue which handles the customers and send them as messages to anyone who may listen.
    //The name of the queue is defined right here (not in some interface on RabbitMQ).
    public static final String CUSTOMER_UPSERT_QUEUE = "customerUpsert";
    public static final String CUSTOMER_DELETE_QUEUE = "customerDelete";
    public static final String CUSTOMER_GETONE_QUEUE = "customerGetOne";
    public static final String CUSTOMER_GETALL_QUEUE = "customerGetAll";
    public static final String CUSTOMER_FINDBYLASTNAME_QUEUE = "customerFindByLastName";
    public static final String CUSTOMER_CREATEBILL = "customerCreateBill";
    public static final String CUSTOMER_GETONEBILL_QUEUE = "customerGetOneBill";

    /**
     * Do any work with a channel.
     * Declares queues and binds queues to exchange.
     * @param channel The channel to use
     * @throws IOException If any error occurs
     */
    @Override
    public void initialize(final Channel channel) throws IOException {

        //RabbitMQ exchange name as defined in g03-restinterface CustomerClientSync.
        String exchangeName = "g03";

        //routing keys as defined in g03-restinterface CustomerClientSync interface
        //the g03-restinterface acts as gateway between the http frontend and the services. It uses RabbitMQ to handle
        //communication to the services and http to talk to the frontend.
        // In this role, g03-restinterface sets up an exchange on RabbitMQ.
        channel.exchangeDeclare(exchangeName, "direct", true);

        //the durable attributes means that the queue persists in RabbitMQ.
        String routingKeyCustomerUpsert = "customer.upsert";
        channel.queueDeclare(CUSTOMER_UPSERT_QUEUE, true, false, false, null);
        channel.queueBind(CUSTOMER_UPSERT_QUEUE, exchangeName, routingKeyCustomerUpsert);

        String routingKeyCustomerDelete = "customer.delete";
        channel.queueDeclare(CUSTOMER_DELETE_QUEUE, true, false, false, null);
        channel.queueBind(CUSTOMER_DELETE_QUEUE, exchangeName, routingKeyCustomerDelete);

        String routingKeyCustomerGetOne = "customer.getOneById";
        channel.queueDeclare(CUSTOMER_GETONE_QUEUE, true, false, false, null);
        channel.queueBind(CUSTOMER_GETONE_QUEUE, exchangeName, routingKeyCustomerGetOne);

        String routingKeyCustomerGetAll = "customer.getAll";
        channel.queueDeclare(CUSTOMER_GETALL_QUEUE, true, false, false, null);
        channel.queueBind(CUSTOMER_GETALL_QUEUE, exchangeName, routingKeyCustomerGetAll);

        String routingKeyCustomerFindByLastName = "customer.findByLastName";
        channel.queueDeclare(CUSTOMER_FINDBYLASTNAME_QUEUE, true, false, false, null);
        channel.queueBind(CUSTOMER_FINDBYLASTNAME_QUEUE, exchangeName, routingKeyCustomerFindByLastName);

        String routingKeyCustomerCreateBill = "customer.createBill";
        channel.queueDeclare(CUSTOMER_CREATEBILL, true, false, false, null);
        channel.queueBind(CUSTOMER_CREATEBILL, exchangeName, routingKeyCustomerCreateBill);

        String routingKeyCustomerGetOneBill = "customer.getOneBill";
        channel.queueDeclare(CUSTOMER_GETONEBILL_QUEUE, true, false, false, null);
        channel.queueBind(CUSTOMER_GETONEBILL_QUEUE, exchangeName, routingKeyCustomerGetOneBill);

    }
}