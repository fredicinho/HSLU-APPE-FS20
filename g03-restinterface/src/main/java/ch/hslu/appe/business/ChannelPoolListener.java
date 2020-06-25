package ch.hslu.appe.business;

import com.rabbitmq.client.Channel;
import io.micronaut.configuration.rabbitmq.connect.ChannelInitializer;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public final class ChannelPoolListener extends ChannelInitializer {

    @Override
    public void initialize(final Channel channel) throws IOException {
        String exchangeName = "g03";
        String queueName = "REST response";
        String routingKey = "rest.response";
        channel.exchangeDeclare(exchangeName, "direct", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
    }
}