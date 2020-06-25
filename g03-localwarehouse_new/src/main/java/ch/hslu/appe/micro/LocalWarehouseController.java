package ch.hslu.appe.micro;

import ch.hslu.appe.business.ProductClientAsync;

public class LocalWarehouseController {
    private final ProductClientAsync storageClientAsync;

    public LocalWarehouseController(ProductClientAsync storageClientAsync) {
        this.storageClientAsync = storageClientAsync;
    }

    public void sendReorderRequest(String id){
        storageClientAsync.sendReorderRequest(id);
    }
}
