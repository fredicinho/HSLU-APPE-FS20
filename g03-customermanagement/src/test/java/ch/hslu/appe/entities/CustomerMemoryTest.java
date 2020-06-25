package ch.hslu.appe.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerMemoryTest {

    private CustomerCrudOperations customerCrudOperations;

    @BeforeEach
    void setUp() {
        this.customerCrudOperations = new CustomerMemory();
        assertThat(new CustomerMemory().getAll()).hasSize(4);
    }

    @Test
    void getOneById() {
        final Customer customer = this.customerCrudOperations.getOneById("5ea16f38b44e99570ddfaeb1");
        assertThat(customer).isNotNull();
        assertThat(customer.getUuid()).isEqualTo("5ea16f38b44e99570ddfaeb1");
        assertThat(customer.getFirstName()).isEqualTo("Skyler");
    }

    @Test
    void findByLastName() {
        final Collection<Customer> customers = this.customerCrudOperations.findByLastName("White");
        assertThat(customers).isNotNull();
        assertThat(customers).hasSize(2);
        assertThat(customers.stream().allMatch(t -> t.getLastName().equals("White"))).isTrue();
        assertThat(customers.stream().anyMatch(t -> t.getFirstName().equals("Skyler"))).isTrue();
        assertThat(customers.stream().anyMatch(t -> t.getFirstName().equals("Walter"))).isTrue();
    }

    @Test
    void getAll() {
        final Collection<Customer> customers = this.customerCrudOperations.getAll();
        assertThat(customers).hasSize(4);
        List<String> ids = new ArrayList<>();
        //Strichliste
        ids.add("5ea16f38b44e99570ddfaeb1");
        ids.add("5ea16f38b44e99570ddbaeb2");
        ids.add("5ea16f38b44e99570ddcaeb3");
        ids.add("5ea16f38b44e99570ddaaeb4");

        for (Customer customer : customers) {
            ids.remove(customer.getUuid());
        }
        assertThat(ids.isEmpty()).isTrue();
    }

    @Test
    void upsert() {
        Customer customer = Customer.builder().firstName("Vorname").lastName("Nachname").
                streetName("StreetName").streetNumber("12b").zip("8001").city("city").email("email").phone("phone")
                .build();
        final Customer customerB = this.customerCrudOperations.upsert(customer);
        assertThat(this.customerCrudOperations.getAll()).hasSize(5);
        assertThat(customerB.getLastName().equals("Nachname")).isTrue();
    }

    @Test
    void delete() {
        customerCrudOperations.delete("5ea16f38b44e99570ddaaeb4");
        assertThat(this.customerCrudOperations.getAll()).hasSize(3);
        assertThat((this.customerCrudOperations.getAll()).stream().noneMatch(t -> t.getLastName().equals("McGill")))
                .isTrue();
    }
}