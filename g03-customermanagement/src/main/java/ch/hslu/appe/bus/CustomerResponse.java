package ch.hslu.appe.bus;

import ch.hslu.appe.entities.Customer;

import java.util.Collection;

/**
 * Java data object (POJO) to send as a response to frontend.
 */
public class CustomerResponse {

    private Customer customer;



    private Collection<Customer> customers;
    private boolean success;
    private String message;

    /**
     * @param customer to be stored in the CustomerResponse so that it can then be sent back to the frontend.
     */
    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the customer object used by the micronaut serializer, shipping it to the frontend.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @return true if the requested transaction terminated succeeded.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success define if the requested transaction succeeded.
     */
    public void setSuccess(final boolean success) {
        this.success = success;
    }

    /**
     * @return the meta-information about the transaction.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message defines the meta-information about the transaction.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * @param customers the customers to be handed over to front end.
     */
    public void setCustomers(final Collection<Customer> customers) {
        this.customers = customers;
    }

    /**
     * @return the list of customers used by the micronaut serializer, shipping it to the frontend.
     */
    public Collection<Customer> getCustomers() {
        return customers;
    }

}
