package ch.hslu.appe.business;

import ch.hslu.appe.bus.CustomerListener;
import ch.hslu.appe.entities.Bill;
import ch.hslu.appe.entities.BillCrudOperations;
import ch.hslu.appe.entities.Customer;
import ch.hslu.appe.entities.OrderPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BillBusinessLogicTest {

    private CustomerListener customerListener;
    private CustomerBusinessLogic customerBusinessLogic;
    private BillBusinessLogic billBusinessLogic;
    private BillCrudOperations billCrudOperations;

    @BeforeEach
    void setUp() {

        this.billCrudOperations = mock(BillCrudOperations.class);

    }

    @Test
    void create() {
        //given
        final OrderPosition orderPosition1= new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        final List<OrderPosition> articleList = new ArrayList<>();
        articleList.add(orderPosition1);
        final Bill bill = new Bill("5ecbbe074d75d712af925447", "7b238395-b905-4cb0-b0d5-6bc6133b3488", articleList, 47.55);
        ArgumentCaptor<Bill> argument = ArgumentCaptor.forClass(Bill.class);
        Customer skyler = Customer.builder().firstName("Skyler").lastName("White")
                .streetName("Wyoming Bldv").streetNumber("1400").zip("8001").city("Albuquerque")
                .email("skyler.white@breakingbad.com").phone("+41787894501").build();
        billBusinessLogic = new BillBusinessLogic(billCrudOperations);
        //when
        billBusinessLogic.create(bill, skyler);
        //then
        verify(billCrudOperations).create(argument.capture());
        assertEquals("5ecbbe074d75d712af925447", argument.getValue().getOrderID());
        assertEquals("Skyler", argument.getValue().getFirstName());
    }
}
