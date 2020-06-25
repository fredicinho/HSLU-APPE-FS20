package ch.hslu.appe.micro;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderConfirmationControllerTestIT {
    private static final String BASE_URL = "http://restinterface.appe-g03.el.eee.intern/api/v1/order/confirmation/";

    @Test
    void testSearchorderConfirmation() throws IOException, InterruptedException {
        final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "?orderConfirmationID=5ec78e167cf70864120179f1")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"customerLastName\":\"Fischer\"");
    }
}
