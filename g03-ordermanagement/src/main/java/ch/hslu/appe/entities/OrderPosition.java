package ch.hslu.appe.entities;

import java.util.Objects;

public final class OrderPosition {
    private String articleID;
    private int count;
    private boolean available;
    private double price;

    private OrderPosition() { }

    public OrderPosition(final String articleID, final int count) {
        this.articleID = articleID;
        this.count = count;
        this.available = false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderPosition that = (OrderPosition) o;
        return count == that.count
                && Objects.equals(articleID, that.articleID) && this.available == that.available
                && Objects.equals(this.price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleID, count);
    }

    @Override
    public String toString() {
        return "OrderPosition{"
                + "articleID='" + articleID + '\''
                + ", count=" + count
                + '}';
    }

    public boolean getAvailable() {
        return this.available;
    }

    public String getArticleID() {
        return articleID;
    }

    public int getCount() {
        return count;
    }

    public double getPrice() {
        return this.price;
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

}
