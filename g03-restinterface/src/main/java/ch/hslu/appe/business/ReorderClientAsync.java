package ch.hslu.appe.business;

import io.micronaut.configuration.rabbitmq.annotation.Binding;

public interface ReorderClientAsync {
    @Binding("storage.insertReorder")
    void insertReorderIntoWarehouse(String id);
}
