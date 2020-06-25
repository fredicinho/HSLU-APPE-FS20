package ch.hslu.appe.bus;

import ch.hslu.appe.business.BillBusinessLogic;
import ch.hslu.appe.business.CustomerBusinessLogic;
import ch.hslu.appe.business.IncompleteDataException;
import ch.hslu.appe.entities.Bill;
import ch.hslu.appe.entities.Customer;
import ch.hslu.appe.entities.OrderPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerListenerTest {
    private CustomerListener customerListener;
    private CustomerBusinessLogic customerBusinessLogic;
    private BillBusinessLogic billBusinessLogic;

    @BeforeEach
    void setUp() {

        this.customerBusinessLogic = mock(CustomerBusinessLogic.class);
        this.billBusinessLogic = mock(BillBusinessLogic.class);
        this.customerListener = new CustomerListener(customerBusinessLogic, billBusinessLogic);

    }



    @Test
    void testUpsertIncompleteData() {
        //given:
        String firstName = "firstName";
        List<String> missingFields = Collections.singletonList(firstName);
        IncompleteDataException incompleteDataException = new IncompleteDataException(missingFields);
        when(customerBusinessLogic.upsert(any())).thenThrow(incompleteDataException);
        //when:
        CustomerResponse customerResponse = customerListener.upsert(new Customer());
        //then:
        assertFalse(customerResponse.isSuccess());
        assertTrue(customerResponse.getMessage().contains(firstName));
    }

    @Test
    void testUpsertSuccessful() {
        //given:
        Customer customer = new Customer();
        when(customerBusinessLogic.upsert(any())).thenReturn(customer);
        //when:
        CustomerResponse customerResponse = customerListener.upsert(customer);
        //then:
        assertTrue(customerResponse.isSuccess());
        assertNull(customerResponse.getMessage());
    }

    @Test
    void testUpsertException() {
        //given
        when(customerBusinessLogic.upsert(any())).thenThrow(new RuntimeException("Runtime exception during upsert."));
        //when
        CustomerResponse customerResponse = customerListener.upsert(new Customer());
        //then
        assertFalse(customerResponse.isSuccess());
        assertTrue(customerResponse.getMessage().contains("Could not perform upsert due to exception: "));
    }

    @Test
    void testGetOneByIdSuccess() {
        //given:
        Customer customer = new Customer();
        String uuid = customer.getUuid();
        System.out.println(uuid);
        Collection<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerBusinessLogic.getOneById(any())).thenReturn(customer);
        //when:
        CustomerResponse customerResponse = customerListener.getOneById(uuid);
        //then:
        assertTrue(customerResponse.isSuccess());
        assertNull(customerResponse.getMessage());
    }

    @Test
    void testGetOneByIdIncorrectID() {
        //given:
        String uuid = "uuid";
        List<String> missingFields = Collections.singletonList(uuid);
        IncompleteDataException incompleteDataException = new IncompleteDataException(missingFields);
        when(customerBusinessLogic.getOneById(any())).thenThrow(incompleteDataException);
        System.out.println(incompleteDataException.toString());
        //when:
        CustomerResponse customerResponse = customerListener.getOneById("123");
        //then:
        assertFalse(customerResponse.isSuccess());
        assertTrue(customerResponse.getMessage().contains("Could not perform getOneById. " +
                "The provided id is not valid:"));
    }

    @Test
    void testGetOneByIdException() {
        //given
        when(customerBusinessLogic.getOneById(any())).thenThrow(new RuntimeException("Runtime exception " +
                "during getOneById."));
        //when
        CustomerResponse customerResponse = customerListener.getOneById("123");
        //then
        assertFalse(customerResponse.isSuccess());
        assertTrue(customerResponse.getMessage().contains("Could not perform getOneById due to exception: "));
    }

    @Test
    void testFindByLastNameSuccess() {
        //given:
        Customer customer = new Customer();
        String lastName = customer.getLastName();
        Collection<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerBusinessLogic.findByLastName(any())).thenReturn(customers);
        //when:
        CustomerResponse customerResponse = customerListener.findByLastName(lastName);
        //then:
        assertTrue(customerResponse.isSuccess());
        assertNull(customerResponse.getMessage());
    }

    @Test
    void testFindByLastNameNotFound() {
        //given:
        String lastName = "White";
        //when:
        CustomerResponse customerResponse = customerListener.findByLastName(lastName);
        //then:
        assertTrue(customerResponse.isSuccess());
        assertTrue(customerResponse.getMessage().contains("No customers found by last name: " + lastName));
    }

    @Test
    void testFindByLastNameException() {
        //given
        when(customerBusinessLogic.findByLastName(any())).thenThrow(new RuntimeException("Runtime exception " +
                "during findByLastName."));
        //when
        CustomerResponse customerResponse = customerListener.findByLastName("Schrader");
        //then
        assertFalse(customerResponse.isSuccess());
        assertTrue(customerResponse.getMessage().contains("Could not perform findByLastName due to exception: "));
    }

    @Test
    void testGetAllSuccess() {
        //given:
        Customer customer = new Customer();
        String lastName = customer.getLastName();
        Collection<Customer> customers = new ArrayList<>();
        customers.add(customer);
        when(customerBusinessLogic.getAll()).thenReturn(customers);
        //when:
        CustomerResponse customerResponse = customerListener.getAll(42);
        //then:
        assertTrue(customerResponse.isSuccess());
        assertNull(customerResponse.getMessage());
    }
     @Test
     void testGetAllEmptyList() {
        //when:
         CustomerResponse customerResponse = customerListener.getAll(42);
        //then:
         assertTrue(customerResponse.isSuccess());
         assertTrue(customerResponse.getMessage().contains("Customer collection in data base is empty. " +
                 "No customers found"));
     }

    @Test
    void testGetAllException() {
        //given
        when(customerBusinessLogic.getAll()).thenThrow(new RuntimeException("Runtime exception during getAll."));
        //when
        CustomerResponse customerResponse = customerListener.getAll(42);
        //then
        assertFalse(customerResponse.isSuccess());
        assertTrue(customerResponse.getMessage().contains("CustomerListener could not perform getAll " +
                "due to exception: "));
    }

    /**
     * https://stackoverflow.com/questions/3555472/mockito-verify-method-arguments
     * check if method was called by capturing the argument that was handed over.
     */
    @Test
    void createBill() {
        String json = "{\"orderID\":\"5ecbbe074d75d712af925447\",\"customerID\":" +
                "\"7b54137d-32e0-4d36-9265-e2339b2ba8a8\",\"firstName\":null,\"lastName\":null,\"articleList\":" +
                "[{\"articleID\":\"765f42cc-c319-41c0-9af6-f7c78cfdff7d\",\"count\":3,\"available\":false," +
                "\"price\":235.0}],\"date\":null,\"fullPrice\":705.0,\"_id\":null}";
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        customerListener.createBill(json);
        verify(customerBusinessLogic).getOneById(argument.capture());
        assertEquals("7b54137d-32e0-4d36-9265-e2339b2ba8a8", argument.getValue());
    }

    @Test
    void getOneBillByIDSuccess() throws IOException {
        final OrderPosition orderPosition1= new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        final List<OrderPosition> articleList = new ArrayList<>();
        final Bill bill = new Bill("5ecbbe074d75d712af925447", "7b238395-b905-4cb0-b0d5-6bc6133b3488",
                articleList, 47.55);
        when(billBusinessLogic.getBillByID(any())).thenReturn(bill);
        //when:
        String customerResponse = customerListener.getOneBillByID("123");
        //then:
        assertTrue(customerResponse.contains("CustomerListener retrieved bill: "));
        assertTrue(customerResponse.contains(bill.getOrderID()));
    }

}