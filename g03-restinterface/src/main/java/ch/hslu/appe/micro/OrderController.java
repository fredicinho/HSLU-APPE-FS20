package ch.hslu.appe.micro;
import ch.hslu.appe.business.OrderClientSync;
import ch.hslu.appe.entities.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.annotation.Nullable;


@Controller("/api/v1/order")
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private final OrderClientSync orderClientSync;

    public OrderController(final OrderClientSync orderClientSync) {
        this.orderClientSync = orderClientSync;
    }

    /**
     * Request um eine neue Bestellung zu erstellen.
     * @param newOrder neue Bestellung im JSON Format.
     * @return HTTP-Response-Code with received Data
     */
    @Post("/")
    public HttpResponse<?> create(@Body final String newOrder) {
        LOG.info("REST, OrderController, sending new Order: " + newOrder);
        return resolveResponse(this.orderClientSync.create(newOrder));
    }

    /**
     * Order suchen.
     * @param orderID ID der gesuchten Bestellung
     * @return result gefundene Bestellung. Wenn nicht gefunden, kommt eine Fehlermeldung
     */
    @Get("/{?orderID}")
    public HttpResponse<?> searchOrder(@QueryValue("orderID") @Nullable final String orderID) {
        LOG.info("REST, OrderController, Sending Request to get Order with ID: " + orderID);
        return resolveResponse(this.orderClientSync.getOrderByID(orderID));
    }

    /**
     * Method which resolves a received Response and returns a HttpResponse-Object defined by the status.
     * @param response Response-String which the REST received
     * @return HttpResponse-Object defined by received status
     */
    public static HttpResponse<?> resolveResponse(final String response) {
        try {
            Response responseObject = new ObjectMapper().readValue(response, Response.class);
            LOG.info("REST, Received Response with Status: " + responseObject.getStatus()
                    + " and Message: " + responseObject.getMessage());

            switch (responseObject.getStatus()) {
                case OK:
                    return HttpResponse.ok(responseObject.getMessage() + responseObject.getData());

                case BAD_REQUEST:
                    return HttpResponse.badRequest(responseObject.getMessage());

                case PRODUCTS_NOT_AVAILABLE:
                    return HttpResponse.notFound(responseObject.getMessage());

                case ERROR:
                    return HttpResponse.serverError(responseObject.getMessage());

                default:
                    return HttpResponse.serverError("Received unkown Message!");
            }
        } catch (JsonProcessingException e) {
            LOG.error("REST, Couldn't transform Message into Json-Node.");
            e.printStackTrace();
            return HttpResponse.serverError();
        }
    }
}
