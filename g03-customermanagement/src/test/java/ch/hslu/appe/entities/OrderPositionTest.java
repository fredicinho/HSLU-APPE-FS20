package ch.hslu.appe.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderPositionTest {

    /**
     * Test method for default constructor {@link OrderPosition#OrderPosition()}.
     * Testing for null.
     */
    @Test
    final void testOrderPositionDefault() {
        final OrderPosition orderPosition = new OrderPosition();
        assertAll("OrderPosition",
                () -> assertThat(orderPosition.getArticleID()).isEqualTo(null),
                () -> assertThat(orderPosition.getAvailable()).isEqualTo(false),
                () -> assertThat(orderPosition.getCount()).isEqualTo(0),
                () -> assertThat(orderPosition.getPrice()).isEqualTo(0.0));
    }

    /**
     * Test method for constructor {@link OrderPosition#OrderPosition(String, int)}.
     * Testing for null.
     */
    @Test
    final void testOrderPositionStringInt() {
        //given
        final OrderPosition orderPosition = new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        //then
        assertAll("OrderPosition",
                () -> assertThat(orderPosition.getArticleID()).isEqualTo("765f42cc-c319-41c0-9af6-f7c78cfdff7d"),
                () -> assertThat(orderPosition.getAvailable()).isEqualTo(false),
                () -> assertThat(orderPosition.getCount()).isEqualTo(3),
                () -> assertThat(orderPosition.getPrice()).isEqualTo(0.0));
    }

    @Test
    void testOrderPositionSetterGetter() {
        //given
        final OrderPosition orderPosition = new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        //when
        orderPosition.setAvailable(true);
        orderPosition.setPrice(249.65);
        //then
        assertAll("OrderPosition",
                () -> assertThat(orderPosition.getArticleID()).isEqualTo("765f42cc-c319-41c0-9af6-f7c78cfdff7d"),
                () -> assertThat(orderPosition.getAvailable()).isEqualTo(true),
                () -> assertThat(orderPosition.getCount()).isEqualTo(3),
                () -> assertThat(orderPosition.getPrice()).isEqualTo(249.65));
    }

    /**
     * Test method for {@link ch.hslu.appe.entities.OrderPosition#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(OrderPosition.class)
                .withOnlyTheseFields("count", "articleID", "available", "price")
                .suppress(Warning.NONFINAL_FIELDS)
                .suppress(Warning.REFERENCE_EQUALITY).verify();
    }

    /**
     * Test method for {@link ch.hslu.appe.entities.OrderPosition#toString()}.
     */
    @Test
    void testToString() {
        //given
        OrderPosition orderPosition = new OrderPosition("765f42cc-c319-41c0-9af6-f7c78cfdff7d", 3);
        //when

        //then
        assertThat(orderPosition
                .toString())
                .contains("765f42cc-c319-41c0-9af6-f7c78cfdff7d")
                .contains("3");
    }
}