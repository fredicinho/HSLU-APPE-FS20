package ch.hslu.appe.business;

import ch.hslu.appe.entities.Customer;
import ch.hslu.appe.entities.CustomerMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerBusinessLogicTest {

    private CustomerMemory customerMemory;
    private CustomerBusinessLogic customerBusinessLogic;

    @BeforeEach
    void setup() {
       this.customerMemory = new CustomerMemory();
       this.customerBusinessLogic = new CustomerBusinessLogic(this.customerMemory);
    }


    @Test
    void testUpsertWithoutId() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White").
                streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        Customer customerFromDB = customerBusinessLogic.upsert(skyler);
        String id = customerFromDB.getUuid();
        Customer skylerUpdated = Customer.builder().uuid(id).firstName("SkylerUpdated").lastName("White").
                streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        Customer skylerUpdatedFromDB = customerBusinessLogic.upsert(skylerUpdated);
        assertThat(skylerUpdatedFromDB).isNotNull();
        assertThat(skylerUpdatedFromDB.getEmail()).isEqualTo("skyler.white@breakingbad.com");
        assertThat(skylerUpdatedFromDB.getFirstName()).isEqualTo("SkylerUpdated");

    }

    @Test
    void testUpsertWithId() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White").
                streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        Customer customerFromDB = customerBusinessLogic.upsert(skyler);
        String uuid = customerFromDB.getUuid();
        Customer skylerUpdated = Customer.builder().uuid(uuid).firstName("SkylerUpdated").lastName("White").
                streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        Customer skylerUpdatedFromDB = customerBusinessLogic.upsert(skylerUpdated);
        assertThat(skylerUpdatedFromDB).isNotNull();
        assertThat(skylerUpdatedFromDB.getUuid()).isEqualTo(uuid);
        assertThat(skylerUpdatedFromDB.getEmail()).isEqualTo("skyler.white@breakingbad.com");
        assertThat(skylerUpdatedFromDB.getFirstName()).isEqualTo("SkylerUpdated");

    }

    @Test
    void testUpsertWithoutFirstName() {
        Customer skyler = Customer.builder().lastName("White").
                streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
       List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
           customerBusinessLogic.upsert(skyler); }).getMissingFields();
       System.out.println(exceptions);
       String expectedMessage = "firstName";
       String actualMessage = exceptions.toString();
       System.out.println(actualMessage);
       assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpsertWithoutLastName() {
        Customer skyler = Customer.builder().firstName("Skyler").
                streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.upsert(skyler); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "lastName";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpserLastNameBlank() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName(" ")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.upsert(skyler); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "lastName";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpsertWithoutStreetNameNumberCity() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .zip("8001")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.upsert(skyler); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "streetName, streetNumber, city";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testUpsertZipBelowRange() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("1").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.upsert(skyler); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "zipCode";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testUpsertZipAboveRange() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("10000").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.upsert(skyler); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "zipCode";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testUpsertZipEmpty() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip(" ").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+4177 123 4567").build();
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.upsert(skyler); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "zipCode";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpsertWithoutEmail() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .phone("+4177 123 4567").build();
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.upsert(skyler); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "email";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpsertWrongEmailFormat() {
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.whitebreakingbad.com").phone("+4177 123 4567").build();
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.upsert(skyler); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "email";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {
        assertTrue(customerBusinessLogic.delete("5ea16f38b44e99570ddbaeb2"));
    }

    @Test
    void testDeleteInexistent() {
        assertFalse(customerBusinessLogic.delete("5ea16f38b44e99570ddbaeb7"));
    }

    @Test
    void testDeleteInvalidId() {
        assertFalse(customerBusinessLogic.delete("5ea16f3baeb7"));
    }

    @Test
    void testDeleteNullId() {
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.delete(null); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "ID";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testGetOneById() {
        Customer james = customerBusinessLogic.getOneById("5ea16f38b44e99570ddaaeb4");
        assertThat(james).isNotNull();
        assertThat(james.getEmail()).isEqualTo("james.mcgill@breakingbad.com");
        assertThat(james.getFirstName()).isEqualTo("James");
    }

    @Test
    void testGetOneByIdEmpty() {
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.getOneById(""); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "ID";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testFindByLastName() {
        Collection<Customer> customers;
        customers = customerBusinessLogic.findByLastName("Pinkman");
        assertThat(customers).isNotNull();
        assertThat(customers.toString().contains("jesse.pinkman@breakingbad.com")).isTrue();
    }
    @Test
    void testFindByLastNameNotInDB() {
        Collection<Customer> customers;
        customers = customerBusinessLogic.findByLastName("Schrader");
        assertThat(customers).isEmpty();
    }

    @Test
    void testFindByLastNameEmpty() {
        List<String> exceptions = assertThrows(IncompleteDataException.class, () -> {
            customerBusinessLogic.findByLastName(""); }).getMissingFields();
        System.out.println(exceptions);
        String expectedMessage = "lastName";
        String actualMessage = exceptions.toString();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getAll() {
        Collection<Customer> customers;
        customers = customerBusinessLogic.getAll();
        assertThat(customers).hasSize(4);
        assertThat(customers.toString()).contains("McGill");
    }
}