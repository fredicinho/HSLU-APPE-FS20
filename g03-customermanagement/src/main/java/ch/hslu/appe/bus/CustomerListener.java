package ch.hslu.appe.bus;

import ch.hslu.appe.business.BillBusinessLogic;
import ch.hslu.appe.business.ChannelPoolListener;
import ch.hslu.appe.business.CustomerBusinessLogic;
import ch.hslu.appe.business.IncompleteDataException;
import ch.hslu.appe.entities.Bill;
import ch.hslu.appe.entities.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.configuration.rabbitmq.annotation.Queue;
import io.micronaut.configuration.rabbitmq.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

/**
 * Listens to the customerRegister queue on RabbitMQ. The queue sends customers as messages to anyone who listens.
 * We can only listen to existing queues. This queue here is defined and initialized in the ChannelPoolListener.
 * The registerCustomer() method hands customers to CustomerCrudOperations which then writes the JSON file to the DB.
 */

//annotation makes this to a class that listens for messages from RabbitMQ
@RabbitListener
public class CustomerListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerListener.class);
    private final CustomerBusinessLogic customerBusinessLogic;
    private final BillBusinessLogic billBusinessLogic;

    public CustomerListener(final CustomerBusinessLogic customerBusinessLogic,
                            final BillBusinessLogic billBusinessLogic) {
        this.customerBusinessLogic = customerBusinessLogic;
        this.billBusinessLogic = billBusinessLogic;
    }

    /**
     * The queue annotation is required for a method to be a consumer of messages from RabbitMQ.
     * The queues we want to listen to have been defined and setup in the initialize method of the ChannelPoolListener.
     * @param customer the customer to be entered into the database.
     * @return customer response, a Pojo containing the customer(s), boolean success and a message.
     */
    @Queue(ChannelPoolListener.CUSTOMER_UPSERT_QUEUE)
    public CustomerResponse upsert(final Customer customer) {
        CustomerResponse customerResponse = new CustomerResponse();
        try {
            Customer returnCustomer = customerBusinessLogic.upsert(customer);
            customerResponse.setCustomer(returnCustomer);
            customerResponse.setSuccess(true);
        } catch (IncompleteDataException ex) {
            customerResponse.setMessage("Could not perform upsert. The following fields are missing data: "
                    + ex.getMissingFields().toString());
            customerResponse.setSuccess(false);
        } catch (Exception ex) {
            LOGGER.error("Exception during upsert", ex);
            customerResponse.setMessage("Could not perform upsert due to exception: " + ex.toString());
            customerResponse.setSuccess(false);
        }
        return customerResponse;
    }

    /**
     * Removes a customer from the database.
     * @param uuid of the customer.
     * @return true if deletion was successful.
     */
    @Queue(ChannelPoolListener.CUSTOMER_DELETE_QUEUE)
    public Boolean delete(final String uuid) {
        return customerBusinessLogic.delete(uuid);
    }

    /**
     * Returns a specific customer by ID.
     * @param uuid customer ID.
     * @return Customer object. Returns null is no match is found.
     */
    @Queue(ChannelPoolListener.CUSTOMER_GETONE_QUEUE)
    public CustomerResponse getOneById(final String uuid) {
        CustomerResponse customerResponse = new CustomerResponse();
        try {
            Customer returnCustomer = customerBusinessLogic.getOneById(uuid);
            customerResponse.setCustomer(returnCustomer);
            customerResponse.setSuccess(true);
        } catch (IncompleteDataException ex) {
            customerResponse.setMessage("Could not perform getOneById. The provided id is not valid: " + uuid);
            customerResponse.setSuccess(false);
        } catch (Exception ex) {
            LOGGER.error("Exception during getOneById", ex);
            customerResponse.setMessage("Could not perform getOneById due to exception: " + ex.toString());
            customerResponse.setSuccess(false);
        }
        return customerResponse;
    }
    /**
     * Returns a list of customers by last name.
     * @param lastName of this customer.
     * @return List of customers by that last name. Returns an empty list if no match is found.
     * Returns a message if customer parameter was an empty String.
     */
    @Queue(ChannelPoolListener.CUSTOMER_FINDBYLASTNAME_QUEUE)
    public CustomerResponse findByLastName(final String lastName) {
        CustomerResponse customerResponse = new CustomerResponse();
        try {
            Collection<Customer> customers = customerBusinessLogic.findByLastName(lastName);
            int size = customers.size();
            customerResponse.setCustomers(customers);
            customerResponse.setSuccess(true);
            if (size == 0) {
                LOGGER.info("No customers found by last name: {}", lastName);
                customerResponse.setMessage("No customers found by last name: " + lastName);
            }
        } catch (Exception ex) {
            LOGGER.error("Exception during findByLastName", ex);
            customerResponse.setMessage("Could not perform findByLastName due to exception: " + ex.toString());
            customerResponse.setSuccess(false);
        }
        return customerResponse;
    }


    /**
     * @param answerToEverything can be null. Any parameter provided will be ignored by getAll() method.
     *                           The parameter is only used here to keep RabbitMQ happy.
     *                           (Unsure why RabbitMQ needs a parameter to every method, but it doesn't work otherwise.)
     * @return all customers in the data base
     */
    @Queue(ChannelPoolListener.CUSTOMER_GETALL_QUEUE)
    public CustomerResponse getAll(final int answerToEverything) {
        CustomerResponse customerResponse = new CustomerResponse();
        LOGGER.info("getAll");
        try {
            Collection<Customer> customers = customerBusinessLogic.getAll();
            int size = customers.size();
            customerResponse.setCustomers(customers);
            customerResponse.setSuccess(true);
            if (size == 0) {
                customerResponse.setMessage("Customer collection in data base is empty. No customers found.");
            }
        } catch (Exception ex) {
            LOGGER.error("Exception during getAll", ex);
            customerResponse.setMessage("CustomerListener could not perform getAll due to exception: " + ex.toString());
            customerResponse.setSuccess(false);
        }
        LOGGER.info(customerResponse.toString());
        return customerResponse;
    }

    /**
     * Creates a bill.
     * @param billAsJson the information required to create the bill.
     */

    @Queue(ChannelPoolListener.CUSTOMER_CREATEBILL)
    public void createBill(final String billAsJson) {
        LOGGER.info("Received new Bill: {}", billAsJson);
        try {
            Bill receivedBill = new ObjectMapper().readValue(billAsJson, Bill.class);
            Customer customer = this.customerBusinessLogic.getOneById(receivedBill.getCustomerID());
            this.billBusinessLogic.create(receivedBill, customer);
        } catch (JsonProcessingException e) {
            LOGGER.info("Couldn't parse Bill as Json into Bill-Object! "
                    + "Bill couldn't be created. Exception: {}", e.toString());
        }
    }

    /**
     * @param id the bill id.
     * @return the bill matching provided id.
     * @throws JsonProcessingException if the JSON cannot be processed properly.
     */
    @Queue(ChannelPoolListener.CUSTOMER_GETONEBILL_QUEUE)
    public String getOneBillByID(final String id) throws JsonProcessingException {
        LOGGER.info("CustomerListener received bill ID to search for Bill: {}", id);
        CustomerResponse customerResponse = new CustomerResponse();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Bill foundedBill = this.billBusinessLogic.getBillByID(id);
            customerResponse.setSuccess(true);
            customerResponse.setMessage("CustomerListener retrieved bill: " + mapper.writeValueAsString(foundedBill));
            return mapper.writeValueAsString(customerResponse);
        } catch (IOException e) {
            LOGGER.error("Problems with converting JSON to Bill-Object."
                    + " Setting CustomerResponse to false. {}", e.toString());
            customerResponse.setSuccess(false);
            customerResponse.setMessage("No Bill found");
            return mapper.writeValueAsString(customerResponse);
        }
    }

}
