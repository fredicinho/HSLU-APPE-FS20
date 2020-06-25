package ch.hslu.appe.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The bill object containing customer and bill information.
 */

public final class Bill {
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String orderID;
    private String customerID;
    private String firstName;
    private String lastName;
    private List<OrderPosition> articleList;
    private Date date;
    private double fullPrice;


    public Bill(final String orderID, final String customerID,
                final List<OrderPosition> articleList, final double fullPrice) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.articleList = articleList;
        this.fullPrice = fullPrice;
    }

    public Bill() {
    }

    /**
     * If price, orderID, customerID, articleList, and date are identical, the 2 are one and the same object.
     * @param o the object to be compared.
     * @return true if the objects are identical.
     */
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

    /**
     * @return returns a hashCode based on order id, customer id, date, and the price.
     */
    @Override
    public int hashCode() {
        return Objects.hash(orderID, customerID, articleList, date, fullPrice);
    }

    /**
     * Returns a String representation of the bill. {@inheritDoc}.
     */
    @Override
    public String toString() {
        return "Bill[id=" + id
                + ", orderID='" + orderID
                + "', customerID='" + customerID
                + "', firstName='" + firstName
                + "', lastName='" + lastName
                + "', articleList=" + articleList
                + "', date=" + date
                + "', fullPrice=" + fullPrice
                + "']";
    }

    /**
     * @return the object id.
     */
    @JsonProperty("_id")
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public ObjectId getId() {
        return id;
    }

    /**
     * @param id the object id to be set.
     */
    public void setId(final ObjectId id) {
        this.id = id;
    }


    /**
     * @return the first name of the customer.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the first name of the customer.
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the last name of the customer.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the last name of the customer.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the order id.
     */
    public String getOrderID() {
        return orderID;
    }

    /**
     * @param orderID the order id to be set.
     */
    public void setOrderID(final String orderID) {
        this.orderID = orderID;
    }

    /**
     * @return the customer id.
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customer id to be set.
     */
    public void setCustomerID(final String customerID) {
        this.customerID = customerID;
    }

    /**
     * @return a list of the articles ordered.
     */
    public List<OrderPosition> getArticleList() {
        return articleList;
    }

    /**
     * @param articleList the list of articles to be ordered.
     */
    public void setArticleList(final List<OrderPosition> articleList) {
        this.articleList = articleList;
    }

    /**
     * @return the amount to be paid.
     */
    public double getFullPrice() {
        return fullPrice;
    }


    /**
     * @param fullPrice the amount to be paid.
     */
    public void setFullPrice(final double fullPrice) {
        this.fullPrice = fullPrice;
    }

    /**
     * @return the date the bill was created.
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date the bill was created.
     */
    public void setDate(final Date date) {
        this.date = date;
    }
}
