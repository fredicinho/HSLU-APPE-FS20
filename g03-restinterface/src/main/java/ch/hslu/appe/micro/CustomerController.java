package ch.hslu.appe.micro;
import javax.annotation.Nullable;
import ch.hslu.appe.business.CustomerClientSync;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller für Customers.
 */
@Controller("/api/v1/customers")
public class CustomerController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerClientSync customerClientSync;

    public CustomerController(final CustomerClientSync customerClientSync) {
        this.customerClientSync = customerClientSync;
    }

    /**
     * Request um einen neuen Kunden zu erstellen.
     * @param newCustomerInJSON neuer Kunde im JSON Format.
     * @return HTTP-Response-Code with received Data
     */
    @Post("/")
    public HttpResponse<?> upsert(@Body final String newCustomerInJSON) {
        String customerFromDB = customerClientSync.upsert(newCustomerInJSON);
        LOG.info("REST, CustomerController, Received customer through Upsert from customerservice: " + customerFromDB);
        return HttpResponse.ok(customerFromDB);
    }

    /**
     * Kunden suchen.
     * @param id der gesuchten Kunden.
     * @return Kunde aus der Datenbank.
     */
    @Get("/{id}")
    public HttpResponse<?> getOneById(@QueryValue("id") final String id) {
        LOG.info("REST, CustomerController, Requesting Customer by Id: " + id);
        String customerFromDB = customerClientSync.getOneById(id);
        LOG.info("REST, CustomerController, Received customer from customer service: " + customerFromDB);
        return HttpResponse.ok(customerFromDB);
    }

    /**
     * Kunden suchen.
     * @param lastName der gesuchten Kunden. Optional. Wenn kein Nachname mitgegeben wird,
     *        werden alle Kunden zurückgegeben.
     * @return Liste aller Kunden mit diesem Nachnamen aus der Datenbank. Wenn lastName null ist,
     *         werden alle Kunden zurückgegeben.
     */
    @Get("{?last_name}")
    public HttpResponse<?> getCustomers(@QueryValue("last_name") @Nullable final String lastName) {
        if (lastName == null) {
            LOG.info("REST, CustomerController, Requesting all customers");
            String customers = customerClientSync.getAll(42);
            LOG.info("REST, CustomerController, Received customers from customer service.");
            return HttpResponse.ok(customers);
        } else {
            LOG.info("REST, CustomerController, Requesting by last name: " + lastName);
            String customers = customerClientSync.findByLastName(lastName);
            LOG.info("REST, CustomerController, Received customer from customer service: " + customers);
            return HttpResponse.ok(customers);
        }

    }
}
