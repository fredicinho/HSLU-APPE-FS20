package ch.hslu.appe.entities;

import java.util.Objects;

public final class OrderPosition {
    private String articleID;
    private int count;
    private boolean available;
    private double price;

    public OrderPosition() { }

    public OrderPosition(final String articleID, final int count) {
        this.articleID = articleID;
        this.count = count;
        this.available = false;
    }

    /**
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
        OrderPosition that = (OrderPosition) o;
        return count == that.count
                && Objects.equals(articleID, that.articleID)
                && this.available == that.available && Objects.equals(this.price, that.price);
    }

    /**
     * @return the hash code created from article id and count.
     */
    @Override
    public int hashCode() {
        return Objects.hash(articleID, count, available, price);
    }

    /**
     * @return Returns a String representation of the order position.
     */
    @Override
    public String toString() {
        return "OrderPosition{"
                + "articleID='" + articleID + '\''
                + ", count=" + count
                + '}';
    }

    /**
     * @return true if available.
     */
    public boolean getAvailable() {
        return this.available;
    }

    /**
     * @return article id.
     */
    public String getArticleID() {
        return articleID;
    }

    /**
     * @return number of available articles.
     */
    public int getCount() {
        return count;
    }

    /**
     * @return price of article.
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * @param available true if available.
     */
    public void setAvailable(final boolean available) {
        this.available = available;
    }

    /**
     * @param price of article.
     */
    public void setPrice(final double price) {
        this.price = price;
    }

}
