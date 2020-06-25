package ch.hslu.appe.business;
import io.micronaut.configuration.rabbitmq.annotation.Binding;
import io.micronaut.configuration.rabbitmq.annotation.RabbitClient;
import io.micronaut.configuration.rabbitmq.annotation.RabbitProperty;


@RabbitClient("g03")
@RabbitProperty(name = "replyTo", value = "amq.rabbitmq.reply-to")
public interface OrderClientSync {

    /**
     * Methode welche beim OrderService einen Order holt.
     * @param orderID Gesuchte Order mit entsprechender orderID.
     * @return order Gefundener Order. Wenn es nicht gefunden wurde, wird eine leere Menge zurückgegeben.
     */
    @Binding("order.getone")
    String getOrderByID(String orderID);

    /**
     * Methode welche beim OrderService einen neuen Order erstellt.
     * @param order Neu erstelter Order.
     * @return response Response Objekt
     *
     */
    @Binding("order.neworder")
    String create(String order);

    /**
     * Methode welche beim OrderService eine Bestellbestätigung holt.
     * @param orderConfirmationID Gesuchte Bestellbestätigung mit entsprechender orderConfirmationID.
     * @return orderConfirmation Gefundene Bestellbestätigung. Wenn es nicht gefunden wurde,
     * wird eine leere Menge zurückgegeben.
     */
    @Binding("order.getoneconfirmation")
    String getOrderConfirmationID(String orderConfirmationID);

}
