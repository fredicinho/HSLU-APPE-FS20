package ch.hslu.appe.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class OrderPositionTest {

    @Test
    public void testConstructor() {
        OrderPosition orderPosition = new OrderPosition("1", 1);
        assertEquals(1, orderPosition.getCount());
        assertEquals("1", orderPosition.getArticleID());
    }


}
