package ch.hslu.appe.business;


import io.micronaut.configuration.rabbitmq.annotation.Binding;
import io.micronaut.configuration.rabbitmq.annotation.RabbitClient;



@RabbitClient("g03")
public interface CustomerClientAsync {

    /**
     * Methode welche an dem CustomerService einen neuen Kunden im JSON-Format (als String) zustellt.
     * @param customer Customer
     */
    @Binding("customer.register")
    void send(String customer);

}

