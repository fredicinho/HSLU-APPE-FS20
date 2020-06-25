package ch.hslu.appe.entities;

import java.util.Objects;

public final class Response {
    private Status status;
    private String message;
    private String data;

    public Response(final Status status, final String message, final String data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    Response() { }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }

    /**
     * ToString-Method of Response.
     * @return Response as String.
     */
    @Override
    public String toString() {
        return "Response{"
                + "status=" + status
                + ", message='" + message + '\''
                + ", data='" + data + '\''
                + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response response = (Response) o;
        return status == response.status
                && Objects.equals(message, response.message)
                && Objects.equals(data, response.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, data);
    }
}
