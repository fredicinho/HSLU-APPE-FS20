package ch.hslu.appe.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Category {
    @JsonProperty("ID")
    private int ID;
    @JsonProperty("name")
    private String name;

    public Category() {}

    public Category(int id, String name) {
        ID = id;
        this.name = name;
    }
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Category)) return false;
        Category category = (Category) o;
        return ID == category.ID && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name);
    }

    @JsonProperty("categoryID")
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
