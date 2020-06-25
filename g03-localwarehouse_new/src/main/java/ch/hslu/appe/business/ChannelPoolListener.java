package ch.hslu.appe.business;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import io.micronaut.configuration.rabbitmq.connect.ChannelInitializer;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class ChannelPoolListener extends ChannelInitializer {

    @Override
    public void initialize(Channel channel) throws IOException {
        String exchangeName = "g03";

        String queueNameLocal = "localWarehouseOne";
        String queueNameLocal2 = "localWarehouseAll";
        String queueNameLocal3 = "localWarehouseInsert";
        String queueNameLocal4 = "localWarehouseUpdate";
        String queueNameLocal5 = "localWarehouseUpdateNumber";
        String queueNameLocal6 = "localWarehouseOrderRequest";
        String queueNameLocal7 = "localWarehouseOrderGetPrice";

        String routingKeyLocalOne = "storage.one";
        String routingKeyLocalAll = "storage.all";
        String routingKeyLocalInsert = "storage.insert";
        String routingKeyLocalUpdate = "storage.updateWhole";
        String routingKeyLocalUpdateNumber = "storage.updateNumber";
        String routingKeyLocalOrderRequest = "storage.orderrequest";
        String routingKeyLocalOrderGetPrices = "storage.getpriceoforderlist";

        channel.exchangeDeclare(exchangeName, "direct", true);

        channel.queueDeclare(queueNameLocal, true, false, false, null);
        channel.queueBind(queueNameLocal, exchangeName, routingKeyLocalOne);

        channel.queueDeclare(queueNameLocal2, true, false, false, null);
        channel.queueBind(queueNameLocal2, exchangeName, routingKeyLocalAll);

        channel.queueDeclare(queueNameLocal3, true, false, false, null);
        channel.queueBind(queueNameLocal3, exchangeName, routingKeyLocalInsert);

        channel.queueDeclare(queueNameLocal4, true, false, false, null);
        channel.queueBind(queueNameLocal4, exchangeName, routingKeyLocalUpdate);

        channel.queueDeclare(queueNameLocal5, true, false, false, null);
        channel.queueBind(queueNameLocal5, exchangeName, routingKeyLocalUpdateNumber);

        channel.queueDeclare(queueNameLocal6, true, false, false, null);
        channel.queueBind(queueNameLocal6, exchangeName, routingKeyLocalOrderRequest);

        channel.queueDeclare(queueNameLocal7, true, false, false, null);
        channel.queueBind(queueNameLocal7, exchangeName, routingKeyLocalOrderGetPrices);


    }
}