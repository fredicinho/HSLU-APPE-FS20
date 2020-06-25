package ch.hslu.appe.micro;
import ch.hslu.appe.business.OrderClientSync;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

@Controller("/api/v1/order/confirmation")
public class OrderConfirmationController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private final OrderClientSync orderClientSync;

    public OrderConfirmationController(final OrderClientSync orderClientSync) {
        this.orderClientSync = orderClientSync;
    }

    /**
     * Bestellbestätigung suchen.
     * @param orderConfirmationID ID der gesuchten Bestellung
     * @return result gefundene Bestellung. Wenn nicht gefunden, kommt eine Fehlermeldung
     */
    @Get("/{?orderConfirmationID}")
    public HttpResponse<?> searchOrderConfirmation(@QueryValue("orderConfirmationID")
                                                       @Nullable final String orderConfirmationID) {
        LOG.info("REST, OrderConfirmationController, Sending Request to "
                + "get OrderConfirmation with ID: " + orderConfirmationID);
        return OrderController.resolveResponse(this.orderClientSync.getOrderConfirmationID(orderConfirmationID));
    }


}
