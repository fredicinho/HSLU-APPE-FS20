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

public final class Bill {
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String orderID;
    private String customerID;
    private String firstName;
    private String lastName;
    private List<OrderPosition> articleList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    private Date date;
    private double fullPrice;



    public Bill(final String orderID, final String customerID,
                final List<OrderPosition> articleList, final double fullPrice) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.articleList = articleList;
        this.fullPrice = fullPrice;
    }

    Bill() { }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bill bill = (Bill) o;
        return Double.compare(bill.fullPrice, fullPrice) == 0
                && Objects.equals(orderID, bill.orderID)
                && Objects.equals(customerID, bill.customerID)
                && Objects.equals(articleList, bill.articleList)
                && Objects.equals(date, bill.date);
    }

    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderID, customerID, articleList, date, fullPrice);
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(final String orderID) {
        this.orderID = orderID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(final String customerID) {
        this.customerID = customerID;
    }

    public List<OrderPosition> getArticleList() {
        return articleList;
    }

    public void setArticleList(final List<OrderPosition> articleList) {
        this.articleList = articleList;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(final double fullPrice) {
        this.fullPrice = fullPrice;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    public Date getDate() {
        return date;
    }
}
