package ch.hslu.appe.business;

import ch.hslu.appe.entities.Customer;
import ch.hslu.appe.entities.CustomerCrudOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class CustomerBusinessLogic {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerBusinessLogic.class);
    private final CustomerCrudOperations customerCrudOperations;

    public CustomerBusinessLogic(final CustomerCrudOperations customerCrudOperations) {
        this.customerCrudOperations = customerCrudOperations;
    }

    /**
     * Updates an existing customer. Considered existing if we receive an ID.
     * Inserts a new customer. Considered new if we do not receive an ID.
     * @param customer new customer.
     * @return Customer object.
     */
    public Customer upsert(final Customer customer) {
        LOGGER.info("Validating customer before upserting...");
        List<String> missingFields = validate(customer);
       // String test = null;
        //test.getBytes();
        if (!missingFields.isEmpty()) {
            throw new IncompleteDataException(missingFields);
        }
        LOGGER.info("Customer has been validated. Consuming customer in customerUpsert queue. "
                + "Writing to database...: {}, {} {} ",
                customer.getUuid(), customer.getFirstName(), customer.getLastName());
        return customerCrudOperations.upsert(customer);
    }

    /**
     * Removes a customer from the database.
     * @param uuid of the customer
     * @return true if deletion was successful.
     */
    public Boolean delete(final String uuid) {
        List<String> validate = validateId(uuid);
        if (!validate.isEmpty()) {
            throw new IncompleteDataException(validate);
        }
        LOGGER.debug("Consuming customer in customerDelete queue. Removing from database: {}.", uuid);
        return customerCrudOperations.delete(uuid);
    }

    /**
     * Returns a specific customer by ID.
     * @param uuid customer ID.
     * @return Customer object. Returns null is no match is found.
     */
    public Customer getOneById(final String uuid) {
        List<String> validate = validateId(uuid);
        if (!validate.isEmpty()) {
            throw new IncompleteDataException(validate);
        }
        LOGGER.debug("Consuming customer id in customerGetOne queue. Retrieving from database: {}.", uuid);
        return customerCrudOperations.getOneById(uuid);
    }

    /**
     * Returns a list of customers by last name.
     * @param lastName of this customer.
     * @return List of customers by that last name. Returns an empty list if no match is found.
     */
    public Collection<Customer> findByLastName(final String lastName) {
        List<String> validate = validateName(lastName);
        if (!validate.isEmpty()) {
            throw new IncompleteDataException(validate);
        }
        LOGGER.debug("Consuming customer name in customerFindByLastName queue. "
              + "Retrieving customers matching: {}. ", lastName);
        return customerCrudOperations.findByLastName(lastName);
    }

    /**
     * Returns a list of all customers stored in the collection.
     * @return list of customers. Returns empty list if collection is empty.
     */
    public Collection<Customer> getAll() {
        LOGGER.debug("Retrieving all customers from database.");
        return customerCrudOperations.getAll();
    }

    private List<String> validateId(final String id) {
        List<String> result = new ArrayList<>();
        if ((id == null) || (id.isBlank())) {
            LOGGER.warn("ID must contain a value.");
            result.add("ID");
        }
        return result;
    }

    private List<String> validateName(final String lastName) {
        List<String> result = new ArrayList<>();
        if ((lastName == null) || (lastName.isBlank())) {
            LOGGER.warn("lastName must contain a value.");
            result.add("lastName");
        }
        return result;
    }

    /**
     * Checks if any values are null or empty strings.
     * Checks if zip code is within 1000 and 9999.
     * @param customer the customer to be validated.
     * @return a list of attributes which are lacking information.
     */
    private List<String> validate(final Customer customer) {
        List<String> result = new ArrayList<>();

        if ((customer.getFirstName() == null) || (customer.getFirstName().isBlank())) {
            LOGGER.warn("First name must contain a value.");
            result.add("firstName");
        }

        if ((customer.getLastName() == null) || (customer.getLastName().isBlank())) {
            LOGGER.warn("Last name must contain a value.");
            result.add("lastName");
        }
        if ((customer.getStreetName() == null) || (customer.getStreetName().isBlank())) {
            LOGGER.warn("Street name must contain a value.");
            result.add("streetName");
        }
        if ((customer.getStreetNumber() == null) || (customer.getStreetNumber().isBlank())) {
            LOGGER.warn("Street number must contain a value.");
            result.add("streetNumber");
        }

        if ((customer.getCity() == null) || (customer.getCity().isBlank())) {
            LOGGER.warn("City must contain a value.");
            result.add("city");
        }
        if ((customer.getEmail() == null) || (customer.getEmail().isBlank()) || (!customer.getEmail().contains("@"))) {
            LOGGER.warn("Invalid email address.");
            result.add("email");
        }
        //phone number is optional and not validated here.
        if ((customer.getZipCode() == null) || (customer.getZipCode().isBlank())
                || (Integer.parseInt(customer.getZipCode()) < 1000)
                || (Integer.parseInt(customer.getZipCode()) > 9999)) {
            LOGGER.warn("Not a valid zip code in Switzerland.");
            result.add("zipCode");
        }
        return result;
    }

}
