package ch.hslu.appe.entities;

import ch.hslu.appe.mongoDB.ObjectIdDeserializer;
import ch.hslu.appe.mongoDB.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;


import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class Order implements Entity {


    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private Date date;
    private State state;
    private String customerID;
    private List<OrderPosition> orderPositionList;

    public Order() { }

    public Order(final ObjectId id, final Date date, final State state,
                 final String customerID, final List<OrderPosition> orderPositionList) {
        this.id = id;
        this.date = date;
        this.state = state;
        this.customerID = customerID;
        this.orderPositionList = orderPositionList;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    public Date getDate() {
        return date;
    }

    public State getState() {
        return state;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(final String customerID) {
        this.customerID = customerID;
    }

    public List<OrderPosition> getOrderPositionList() {
        return orderPositionList;
    }

    public void setOrderPositionList(final List<OrderPosition> orderPositionList) {
        this.orderPositionList = orderPositionList;
    }


    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return id.equals(order.id)
                && state == order.state
                && customerID.equals(order.customerID)
                && orderPositionList.equals(order.orderPositionList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, state, customerID, orderPositionList);
    }
}
