package ch.hslu.appe.entities;

/**
 * Business logic for bill handling.
 */
public interface BillCrudOperations {
    /**
     * @param newBill a bill object.
     * @return a bill object.
     */
    Bill create(Bill newBill);

    /**
     * @param id the bill id.
     * @return a bill object.
     */
    Bill getBillByID(String id);
}
