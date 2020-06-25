package ch.hslu.appe.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalWarehouseTest {

    @Test
    void testSingleton(){
        LocalWarehouse testhouse = LocalWarehouse.getInstance();
        assertEquals(testhouse.hashCode(), LocalWarehouse.getInstance().hashCode());
    }
}
