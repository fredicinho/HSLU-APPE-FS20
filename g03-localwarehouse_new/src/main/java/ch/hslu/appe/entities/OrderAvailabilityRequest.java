package ch.hslu.appe.entities;

import java.util.List;

public class OrderAvailabilityRequest {
    private List<OrderPosition> orderPositionList;
    private String id;

    public OrderAvailabilityRequest() {}

    public OrderAvailabilityRequest(final List<OrderPosition> orderPositionList, final String id){
        this.orderPositionList = orderPositionList;
        this.id = id;
    }

    public List<OrderPosition> getOrderPositionList() {
        return orderPositionList;
    }

    public String getId() {
        return this.id;
    }
}
