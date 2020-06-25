package ch.hslu.appe.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;


public class CustomerMemory implements CustomerCrudOperations {
    private static final Logger LOG = LogManager.getLogger(CustomerMemory.class);

    private Map<String, Customer> customers;
    private static int idInc = 0;

    /**
     * Erzeugt eine statische Liste mit vier Kunden.
     * */

    public CustomerMemory() {
        if (this.customers == null) {
            this.customers = new HashMap<>();
            this.customers.put("5ea16f38b44e99570ddfaeb1", Customer.builder().uuid("5ea16f38b44e99570ddfaeb1")
                    .firstName("Skyler").lastName("White").streetName("Wyoming Bldv").streetNumber("1400")
                    .zip("8001").city("Albuquerque").email("skyler.white@breakingbad.com").phone("0787894501")
                    .admonitionLevel(AdmonitionLevel.NOTHING).build());
            this.customers.put("5ea16f38b44e99570ddbaeb2", Customer.builder().uuid("5ea16f38b44e99570ddbaeb2")
                    .firstName("Walter").lastName("White").streetName("Tramway Bldv").streetNumber("1501").zip("8002")
                    .city("Albuquerque").email("walter.white@breakingbad.com").phone("0787894502")
                    .admonitionLevel(AdmonitionLevel.FIRST_LEVEL).build());
            this.customers.put("5ea16f38b44e99570ddcaeb3", Customer.builder().uuid("5ea16f38b44e99570ddcaeb3")
                    .firstName("Jesse").lastName("Pinkman").streetName("Street Northwest").streetNumber("322")
                    .zip("8003").city("Albuquerque").email("jesse.pinkman@breakingbad.com").phone("0787894503")
                    .admonitionLevel(AdmonitionLevel.THIRD_LEVEL).build());
            this.customers.put("5ea16f38b44e99570ddaaeb4", Customer.builder().uuid("5ea16f38b44e99570ddaaeb4")
                    .firstName("James").lastName("McGill").streetName("Juan Tabo Blvd. NE").streetNumber("160")
                    .zip("8004").city("Albuquerque").email("james.mcgill@breakingbad.com").phone("0787894504")
                    .admonitionLevel(AdmonitionLevel.SECOND_LEVEL).build());
            idInc = this.customers.size();
            LOG.debug("Es wurden {} Kunden erzeugt.", idInc);
        }
    }


    @Override
    public Customer getOneById(final String id) {
        LOG.info("Retrieved customer with id = {}.", id);
        return this.customers.get(id);
    }

    @Override
    public Collection<Customer> findByLastName(final String lastName) {
        Collection<Customer> list = this.customers.values().stream().filter(s -> s.getLastName().contains(lastName))
                .collect(Collectors.toList());
        LOG.info("Found list of {} customers matching last name {}.", list.size(), lastName);
        return list;
    }

    @Override
    public Collection<Customer> getAll() {
        Collection<Customer> list = this.customers.values().stream().collect(Collectors.toList());
        LOG.info("Retrieved list of {} customers.", list.size());
        return list;
    }

    @Override
    public Customer upsert(final Customer customer) {
        String id = new ObjectId().toString();
        this.customers.put(id, customer);
        LOG.info("Created customer {}. ", customer);
        return customer;
    }

    @Override
    public boolean delete(final String id) {
        final Customer customer = customers.remove(id);
        LOG.info("Deleted customer with id={}.", customer);
        return (customer != null);
    }
}
