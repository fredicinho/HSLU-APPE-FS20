package ch.hslu.appe.business;
import io.micronaut.configuration.rabbitmq.annotation.Binding;
import io.micronaut.configuration.rabbitmq.annotation.RabbitClient;
import io.micronaut.configuration.rabbitmq.annotation.RabbitProperty;

@RabbitClient("g03")
@RabbitProperty(name = "replyTo", value = "amq.rabbitmq.reply-to")
public interface CustomerClientSync {

    /**
     * Requests a list of all customers from the customer service.
     * @param whatever Whatever
     * @return a list of all customer records in JSON format.
     */
    @Binding("customer.getAll")
    String getAll(int whatever);

    /**
     * Requests the data of one specific customer from the customer service.
     * @param customerID ID of the customer to be found.
     * @return the customer record in JSON format.
     */
    @Binding("customer.getOneById")
    String getOneById(String customerID);

    /**
     * Requests a list of all customers from the customer service.
     * @param lastName of the customer to be found.
     * @return a list of all matching customer records in JSON format.
     */
    @Binding("customer.findByLastName")
    String findByLastName(String lastName);


    /**
     * Requests to insert a customer into customer service.
     * If the JSON file does not include an ID, a new customer is created.
     * If the JSON file does include an ID, this record is updated.
     * @param customer customer data in JSON format. It may or may not contain an ID.
     * @return the customer record in JSON format.
     */
    @Binding("customer.upsert")
    String upsert(String customer);

    /**
     * Request to get one Bill which contains the given BillID.
     * Returns a CustomerResponse-Object as JSON
     * @param billID ID of the Bill
     * @return CustomerResponse-Object as JSON where the Bill is in the "Message"-Key
     */
    @Binding("customer.getOneBill")
    String getOneBill(String billID);
}
