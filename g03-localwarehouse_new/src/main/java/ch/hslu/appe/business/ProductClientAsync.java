package ch.hslu.appe.business;

import io.micronaut.configuration.rabbitmq.annotation.Binding;
import io.micronaut.configuration.rabbitmq.annotation.RabbitClient;

@RabbitClient("g03")
public interface ProductClientAsync {

    /**
     * Sends processed Order back to Order-Service
     * @param json Updated Order as JSON
     */
    @Binding("order.availabilityResponse")
    void sendOrderBack(String json);

    @Binding("reorder.article")
    void sendReorderRequest(String id);
}
