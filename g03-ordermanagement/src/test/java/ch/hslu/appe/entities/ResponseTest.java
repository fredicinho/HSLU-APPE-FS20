package ch.hslu.appe.entities;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {

    @Test
    public void testConstructor1() {
        Response response = new Response(Status.ERROR, "message", "data");
        assertEquals(response.getStatus(), Status.ERROR);
        assertEquals(response.getData(), "data");
        assertEquals(response.getMessage(), "message");
    }

    @Test
    public void testSetStatus() {
        Response response = new Response(Status.ERROR, "message", "data");
        response.setStatus(Status.PRODUCTS_NOT_AVAILABLE);
        assertEquals(response.getStatus(), Status.PRODUCTS_NOT_AVAILABLE);
    }

    @Test
    public void testSetMessage() {
        Response response = new Response(Status.ERROR, "message", "data");
        response.setMessage("newMessage");
        assertEquals(response.getMessage(), "newMessage");
    }

    @Test
    public void testSetData() {
        Response response = new Response(Status.ERROR, "message", "data");
        response.setData("newData");
        assertEquals(response.getData(), "newData");
    }
}
