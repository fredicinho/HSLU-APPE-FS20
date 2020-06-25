package ch.hslu.appe.micro;

import ch.hslu.appe.business.CustomerClientSync;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nullable;

/**
 * Controller für Bills.
 */
@Controller("/api/v1/customers/bills")
public class BillController {
    private final CustomerClientSync customerClientSync;
    private static final Logger LOG = LoggerFactory.getLogger(BillController.class);

    public BillController(final CustomerClientSync customerClientSync) {
        this.customerClientSync = customerClientSync;
    }

    /**
     * Getting One bill by given ID.
     * @param billID ID of the Bill
     * @return HTTP_Response with founded Bill as JSON
     */
    @Get("/{?billID}")
    public HttpResponse<?> searchBill(@QueryValue("billID") @Nullable final String billID) {
        LOG.info("REST, BillController, Request to get Bill by ID: " + billID);
        String foundedBill = this.customerClientSync.getOneBill(billID);
        return HttpResponse.ok(foundedBill);
    }



}
