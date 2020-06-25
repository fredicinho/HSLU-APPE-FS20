package ch.hslu.appe.entities;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class Article {

    @JsonProperty("_id")
    private String _id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("numberInStock")
    private int numberInStock;
    @JsonProperty("price")
    private double price;
    @JsonProperty("categoryID")
    private Category category;


    Article() {}

    public Article(String name, int numberInStock, double price, Category category) {
        this.name = name;
        this.numberInStock = numberInStock;
        this.category = category;
        this.price = price;
        this._id = UUID.randomUUID().toString();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberInStock() {
        return numberInStock;
    }


    public void setNumberInStock(int numberInStock) {
        this.numberInStock = numberInStock;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getUniqueID() {
        return _id;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Article)) return false;
        Article article = (Article) o;
        return getUniqueID() == article.getUniqueID() && getNumberInStock() == article.getNumberInStock() && Objects.equals(getName(), article.getName()) && Objects.equals(getCategory(), article.getCategory());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getName(), getNumberInStock(), getCategory());
    }


}
