package ch.hslu.appe.entities;

/**
 * Java data object (POJO) used for serialization.
 * If all parameters are provided in the file (event when one if its values may be null,
 * the constructors are used for deserialization).
 * If a file is sent that lacks a parameter (e.g. no property "email": in JSON), the setters are used.
 * The current implementation no longer validates against a JSON schema. All properties are treated as optional.
 * A missing property is assigned the value "unknown".
 */

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Objects;

public final class Customer {

    @JsonProperty("uuid")
    private String uuid; //optional

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("street")
    private String streetName;

    @JsonProperty("number")
    private String streetNumber;

    @JsonProperty("zip")
    private String zip;

    @JsonProperty("city")
    private String city;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone; //optional

    @JsonProperty("admonitionlevel")
    private AdmonitionLevel admonitionLevel;




    /**
     * Constructor with CustomerBuilder (builder pattern).
     * @param builder the builder used to cunstruct a customer.
     */
    private Customer(final CustomerBuilder builder) {
        this.uuid = builder.uuid;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.streetName = builder.streetName;
        this.streetNumber = builder.streetNumber;
        this.zip = builder.zip;
        this.city = builder.city;
        this.email = builder.email;
        this.phone = builder.phone;
        this.admonitionLevel = builder.admonitionLevel;
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public Customer() {

    }


    @JsonGetter(value = "admonitionLevel")
    public AdmonitionLevel getAdmonitionLevel() {
        return this.admonitionLevel;
    }

    @JsonSetter(value = "admonitionLevel")
    public void setAdmonitionLevel(final AdmonitionLevel admonitionLevel) {
        this.admonitionLevel = admonitionLevel;
    }

    /**
     * @return the uuid
     */
    @JsonGetter(value = "uuid")
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the id to set
     */
    @JsonSetter(value = "uuid")
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the firstName
     */
    @JsonGetter(value = "first_name")
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    @JsonSetter(value = "first_name")
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    @JsonGetter(value = "last_name")
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    @JsonSetter(value = "last_name")
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @JsonGetter(value = "street")
    public String getStreetName() {
        return this.streetName;
    }

    @JsonSetter(value = "street")
    public void setStreetName(final String streetName) {
        this.streetName = streetName;
    }

    @JsonGetter(value = "number")
    public String getStreetNumber() {
        return this.streetNumber;
    }

    @JsonSetter(value = "number")
    public void setStreetNumber(final String streetNumber) {
        this.streetNumber = streetNumber;
    }

    @JsonGetter(value = "zip")
    public String getZipCode() {
        return this.zip;
    }

    @JsonSetter(value = "zip")
    public void setZipCode(final String zipCode) {
        this.zip = zipCode;
    }

    @JsonGetter(value = "city")
    public String getCity() {
        return this.city;
    }

    @JsonSetter(value = "city")
    public void setCity(final String city) {
        this.city = city;
    }


    /**
     * @return the email address of the customer
     */
    @JsonGetter(value = "email")
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    @JsonSetter(value = "email")
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @return the phone number of the customer
     */
    @JsonGetter(value = "phone")
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone number to set
     */
    @JsonSetter(value = "phone")
    public void setPhone(final String phone) {
        this.phone = phone;
    }

    /**
     * If we get 2 identical IDs, the 2 customers are identical. {@inheritDoc}.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }
        return this.uuid == ((Customer) obj).uuid;
    }

    /**
     * Returns a hash code based on the ID. {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.uuid);
    }

    /**
     * Returns a String representation of the customer. {@inheritDoc}.
     */
    @Override
    public String toString() {
        return "Customer[id=" + this.uuid + ", firstName='" + this.firstName + "', lastName='" + this.lastName
                + ", streetName='" + this.streetName + ", streetNumber='" + this.streetNumber
                + ", zip='" + zip + ", city='" + city
                + ", email='" + this.email + "', phone='" + this.phone + "', admonitionLevel='"
                + this.admonitionLevel + "']";
    }


    /**
     * CustomerBuilder class used by Customer.
     */

    public static final class CustomerBuilder {
        private String uuid; //optional
        private String firstName;
        private String lastName;
        private String streetName;
        private String streetNumber;
        private String zip;
        private String city;
        private String email;
        private String phone; //optional
        private AdmonitionLevel admonitionLevel; //optional

        private CustomerBuilder() {

        }

        //methoden für jeden parameter
        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder uuid(@JsonProperty("uuid") final String uuid) {
            this.uuid = uuid;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder firstName(@JsonProperty("first_name") final String firstName) {
            this.firstName = firstName;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder lastName(@JsonProperty("last_name") final String lastName) {
            this.lastName = lastName;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder streetName(@JsonProperty("street") final String streetName) {
            this.streetName = streetName;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder streetNumber(@JsonProperty("number") final String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder zip(@JsonProperty("zip") final String zip) {
            this.zip = zip;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder city(@JsonProperty("city") final String city) {
            this.city = city;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder email(@JsonProperty("email") final String email) {
            this.email = email;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder phone(@JsonProperty("phone") final String phone) {
            this.phone = phone;
            return this;
        }

        @SuppressWarnings("checkstyle:HiddenField")
        public CustomerBuilder admonitionLevel(@JsonProperty("admonitionLevel") final AdmonitionLevel admonitionLevel) {
            this.admonitionLevel = admonitionLevel;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

}
