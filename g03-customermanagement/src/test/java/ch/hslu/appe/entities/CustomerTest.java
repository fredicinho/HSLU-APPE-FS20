package ch.hslu.appe.entities;

import static ch.hslu.appe.entities.AdmonitionLevel.FIRST_LEVEL;
import static ch.hslu.appe.entities.AdmonitionLevel.NOTHING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import java.util.UUID;


/**
 * Testcases for {@link Customer}.
 */

class CustomerTest {


    /**
     * Test method for constructor {@link Customer#Customer()}.
     * Testing for null.
     */
    @Test
    final void testCustomerDefault() {
        final Customer customer = new Customer();
        assertAll("Customer", () -> assertThat(customer.getUuid()).isEqualTo(null),
                () -> assertThat(customer.getFirstName()).isEqualTo(null),
                () -> assertThat(customer.getLastName()).isEqualTo(null),
                () -> assertThat(customer.getStreetName()).isEqualTo(null),
                () -> assertThat(customer.getStreetNumber()).isEqualTo(null),
                () -> assertThat(customer.getZipCode()).isEqualTo(null),
                () -> assertThat(customer.getCity()).isEqualTo(null),
                () -> assertThat(customer.getEmail()).isEqualTo(null),
                () -> assertThat(customer.getPhone()).isEqualTo(null),
                () -> assertThat(customer.getAdmonitionLevel()).isEqualTo(null));
    }

    /**
     * Test method for {@link Customer#Customer()}.
     */
    @Test
    final void testCustomerSetter() {
        final Customer customer = new Customer();
        //new ObjectId() with no arguments generates a unique, hexadecimal ObjectId.
        final String uuid = UUID.randomUUID().toString();


        customer.setUuid(uuid);
        customer.setFirstName("Vorname");
        customer.setLastName("Nachname");
        customer.setStreetName("StreetName");
        customer.setStreetNumber("12b");
        customer.setZipCode("8001");
        customer.setCity("city");
        customer.setEmail("email");
        customer.setPhone("phone");
        customer.setAdmonitionLevel(NOTHING);
        assertAll("Customer", () -> assertThat(customer.getUuid()).isEqualTo(uuid),
                () -> assertThat(customer.getFirstName()).isEqualTo("Vorname"),
                () -> assertThat(customer.getLastName()).isEqualTo("Nachname"),
                () -> assertThat(customer.getStreetName()).isEqualTo("StreetName"),
                () -> assertThat(customer.getStreetNumber()).isEqualTo("12b"),
                () -> assertThat(customer.getZipCode()).isEqualTo("8001"),
                () -> assertThat(customer.getCity()).isEqualTo("city"),
                () -> assertThat(customer.getEmail()).isEqualTo("email"),
                () -> assertThat(customer.getPhone()).isEqualTo("phone"),
                () -> assertThat(customer.getAdmonitionLevel()).isEqualTo(NOTHING));
    }

    /**
     * Test method for {@link ch.hslu.appe.entities.Customer#equals(java.lang.Object)}.
     */
    @Test
    final void testEqualsObject() {
        EqualsVerifier.forClass(Customer.class).withOnlyTheseFields("uuid").suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.REFERENCE_EQUALITY).verify();
    }

    /**
     * Test method for {@link ch.hslu.appe.entities.Customer#toString()}.
     */
    @Test
    final void testToString() {

        Customer customer = Customer.builder().firstName("Vorname").lastName("Nachname").
              streetName("StreetName").streetNumber("12b").zip("8001").city("city").email("email").phone("phone")
                .admonitionLevel(FIRST_LEVEL).build();
        assertThat(customer
                .toString())
                .contains("Vorname")
                .contains("Nachname")
                .contains("StreetName")
                .contains("12b")
                .contains("8001")
                .contains("city")
                .contains("email")
                .contains("phone")
                .contains("FIRST_LEVEL");
    }

    @Test
    public void testCustomerToJson() throws JsonProcessingException {
        Customer customer = Customer.builder().build();
        customer.setAdmonitionLevel(NOTHING);
        ObjectMapper mapper = new ObjectMapper();
        String customerAsJson = mapper.writeValueAsString(customer);
        JsonNode node = mapper.readTree(customerAsJson);
        assertEquals(node.get("admonitionLevel").asText(), NOTHING.toString());
    }


}