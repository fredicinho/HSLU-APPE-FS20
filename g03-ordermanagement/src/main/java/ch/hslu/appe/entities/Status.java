package ch.hslu.appe.entities;

public enum Status {
    OK(200, "Request Successfull"),
    ERROR(500, "Internal error"),
    BAD_REQUEST(400, "Bad request"),
    PRODUCTS_NOT_AVAILABLE(413, "Products not available");

    private int code;
    private String message;

    Status(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    Status() { }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
