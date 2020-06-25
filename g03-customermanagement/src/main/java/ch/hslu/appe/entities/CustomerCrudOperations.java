package ch.hslu.appe.entities;

import java.util.Collection;

/**
 * Business logic for customer administration.
 */
public interface CustomerCrudOperations {

    /**
     * Returns a specific customer by ID.
     * @param id customer ID.
     * @return Customer object. Returns null is no match is found.
     */
    Customer getOneById(String id);

    /**
     * Returns a list of customers by last name.
     * @param lastName of this customer.
     * @return List of customers by that last name. Returns an empty list if no match is found.
     */
    Collection<Customer> findByLastName(String lastName);

    /**
     * Returns a list of all customers stored in the collection.
     * @return list of customers. Returns empty list if collection is empty.
     */
    Collection<Customer> getAll();

    /**
     * Updates an existing customer. considered existing, if we have an ID.
     * Inserts a new customer. Considered new if we do not have an ID to the name.
     * @param customer new customer.
     * @return Customer object.
     */
    Customer upsert(Customer customer);


    /**
     * Deletes existing customer record.
     * @param uuid of the customer.
     * @return Returns true if record was deleted. False if no such record was found.
     */
    boolean delete(String uuid);

}
