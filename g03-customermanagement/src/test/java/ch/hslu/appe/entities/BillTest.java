package ch.hslu.appe.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BillTest {

    /**
     * Test method for default constructor {@link Customer#Customer()}.
     * Testing for null.
     */
    @Test
    final void testBillDefault() {
        final Bill bill = new Bill();

        assertAll("Bill", () -> assertThat(bill.getCustomerID()).isEqualTo(null),
                () -> assertThat(bill.getFirstName()).isEqualTo(null),
                () -> assertThat(bill.getLastName()).isEqualTo(null),
                () -> assertThat(bill.getDate()).isEqualTo(null),
                () -> assertThat(bill.getOrderID()).isEqualTo(null),
                () -> assertThat(bill.getClass()).isEqualTo(Bill.class),
                () -> assertThat(bill.getArticleList()).isEqualTo(null),
                () -> assertThat(bill.getFullPrice()).isEqualTo(0.0),
                () -> assertThat(bill.getId()).isEqualTo(null));
    }

    /**
     * Test method for constructor {@link Bill#Bill(String, String, List, double)}.
     */
    @Test
    final void testBillStringStringListDouble() {
        final OrderPosition orderPosition1= new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        final List<OrderPosition> articleList = new ArrayList<>();
        final Bill bill = new Bill("5ecbbe074d75d712af925447", "7b238395-b905-4cb0-b0d5-6bc6133b3488", articleList, 47.55);
        assertAll("Bill",
                () -> assertThat(bill.getOrderID()).isEqualTo("5ecbbe074d75d712af925447"),
                () -> assertThat(bill.getArticleList()).isEqualTo(articleList),
                () -> assertThat(bill.getFullPrice()).isEqualTo(47.55),
                () -> assertThat(bill.getCustomerID()).isEqualTo("7b238395-b905-4cb0-b0d5-6bc6133b3488"));
    }

    /**
     * Test method for {@link ch.hslu.appe.entities.Bill#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(Bill.class)
                .withOnlyTheseFields("orderID", "customerID", "date", "fullPrice", "articleList")
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.REFERENCE_EQUALITY).verify();
    }


    /**
     * Test method for {@link Bill#Bill(String, String, List, double)} ()}.
     */
    @Test
    void testBillSetter() {
        //given
        final Bill bill = new Bill();
        final ObjectId objectId = new ObjectId();
        final OrderPosition orderPosition1= new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        final List<OrderPosition> articleList = new ArrayList<>();
        final Date date = new Date();

        //when
        bill.setFirstName("Skyler");
        bill.setLastName("White");
        bill.setFullPrice(38.40);
        bill.setOrderID("5ecbbe074d75d712af925447");
        bill.setCustomerID("7b238395-b905-4cb0-b0d5-6bc6133b3488");
        bill.setDate(date);
        bill.setId(objectId);
        articleList.add(orderPosition1);
        bill.setArticleList(articleList);

        //then
        assertAll("Bill", () -> assertThat(bill.getId()).isEqualTo(objectId),
                () -> assertThat(bill.getFirstName()).isEqualTo("Skyler"),
                () -> assertThat(bill.getLastName()).isEqualTo("White"),
                () -> assertThat(bill.getCustomerID()).isEqualTo("7b238395-b905-4cb0-b0d5-6bc6133b3488"),
                () -> assertThat(bill.getArticleList()).isEqualTo(articleList),
                () -> assertThat(bill.getFullPrice()).isEqualTo(38.40),
                () -> assertThat(bill.getClass()).isEqualTo(Bill.class),
                () -> assertThat(bill.getDate()).isEqualTo(date),
                () -> assertThat(bill.getOrderID()).isEqualTo("5ecbbe074d75d712af925447"));
    }

    /**
     * Test method for {@link ch.hslu.appe.entities.Bill#toString()}.
     */
    @Test
    final void testToString() {
        //given
        final Bill bill = new Bill();
        final ObjectId objectId = new ObjectId();
        final OrderPosition orderPosition1 = new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        final List<OrderPosition> articleList = new ArrayList<>();
        final Date date = new Date();

        //when
        bill.setFirstName("Skyler");
        bill.setLastName("White");
        bill.setFullPrice(38.4);
        bill.setOrderID("5ecbbe074d75d712af925447");
        bill.setCustomerID("7b238395-b905-4cb0-b0d5-6bc6133b3488");
        bill.setDate(date);
        bill.setId(objectId);
        articleList.add(orderPosition1);
        bill.setArticleList(articleList);
        //then
        assertThat(bill
                .toString())
                .contains("Skyler")
                .contains("White")
                .contains("38.4")
                .contains("5ecbbe074d75d712af925447")
                .contains("7b238395-b905-4cb0-b0d5-6bc6133b3488")
                .contains(objectId.toString())
                .contains(articleList.toString())
                .contains(date.toString());
    }
}