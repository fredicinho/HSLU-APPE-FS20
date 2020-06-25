package ch.hslu.appe.business;

import ch.hslu.appe.entities.Bill;
import ch.hslu.appe.entities.BillCrudOperations;
import ch.hslu.appe.entities.Customer;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Date;

/**
 * Business logic for bill handling.
 */
@Singleton
public class BillBusinessLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger(BillBusinessLogic.class);
    private final BillCrudOperations billCrudOperations;

    public BillBusinessLogic(final BillCrudOperations billCrudOperations) {
        this.billCrudOperations = billCrudOperations;
    }

    /**
     * @param newBill a bill object.
     * @param customer the customer associated with this bill.
     * @return a bill object.
     */
    public Bill create(final Bill newBill, final Customer customer) {
        newBill.setId(new ObjectId());
        newBill.setDate(new Date());
        newBill.setFirstName(customer.getFirstName());
        newBill.setLastName(customer.getLastName());
        LOGGER.info("BillBusinessLogic: passing bill to billCrudOperations.");
        return billCrudOperations.create(newBill);
    }

    /**
     * @param id the bill id.
     * @return a bill object.
     */
    public Bill getBillByID(final String id) {
        return billCrudOperations.getBillByID(id);
    }

}
