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

public final class OrderConfirmation implements Entity {
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private Date date;
    private String customerID;
    private String orderID;
    private String customerFirstName;
    private String customerLastName;
    private double wholePrice;
    private List<OrderPosition> articleList;

    OrderConfirmation() { }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderConfirmation that = (OrderConfirmation) o;
        return Double.compare(that.wholePrice, wholePrice) == 0
                && Objects.equals(id, that.id)
                && Objects.equals(date, that.date)
                && Objects.equals(customerID, that.customerID)
                && Objects.equals(orderID, that.orderID)
                && Objects.equals(customerFirstName, that.customerFirstName)
                && Objects.equals(customerLastName, that.customerLastName)
                && Objects.equals(articleList, that.articleList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, customerID, orderID, customerFirstName, customerLastName, wholePrice, articleList);
    }

    public OrderConfirmation(final Date date, final String customerID, final String orderID,
                             final String customerFirstName, final String customerLastName,
                             final List<OrderPosition> articleList) {
        this.date = date;
        this.customerID = customerID;
        this.orderID = orderID;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.articleList = articleList;
        this.wholePrice = 0;
        for (OrderPosition position : articleList) {
            double price = position.getPrice() * position.getCount();
            this.wholePrice += price;
        }
    }

    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId getId() {
        return id;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    public Date getDate() {
        return this.date;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public double getWholePrice() {
        return wholePrice;
    }

    public void setWholePrice(final double wholePrice) {
        this.wholePrice = wholePrice;
    }

    public List<OrderPosition> getArticleList() {
        return articleList;
    }

    public void setCustomerID(final String customerID) {
        this.customerID = customerID;
    }

    public void setOrderID(final String orderID) {
        this.orderID = orderID;
    }

    public void setCustomerFirstName(final String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public void setCustomerLastName(final String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public void setArticleList(final List<OrderPosition> articleList) {
        this.articleList = articleList;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

    public void setDate(final Date date) {
        this.date = date;
    }
}
