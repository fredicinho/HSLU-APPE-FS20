package ch.hslu.appe.micro;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.junit.jupiter.api.Test;

public class CustomerControllerTestIT {
    private static final String BASE_URL = "http://restinterface.appe-g03.el.eee.intern/api/v1/customers/";

    @Test
    void testGetOneByID() throws IOException, InterruptedException {
            final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
            final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "2f55ccc2-48a4-4c43-8af3-44694cc4136a")).GET().build();
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.body()).contains("\"uuid\":\"2f55ccc2-48a4-4c43-8af3-44694cc4136a\"");
    }

    @Test
    void testCreateOne() throws IOException, InterruptedException {
        final String newCustomer = "{\n" +
                "  \n" +
                "  \"first_name\": \"Fred\",\n" +
                "  \"last_name\": \"Fischer\",\n" +
                "  \"street\": \"Wyoming Bldv\",\n" +
                "  \"number\": \"1501\",\n" +
                "  \"zip\": \"8001\",\n" +
                "  \"city\": \"Albuquerque\",\n" +
                "  \"email\": \"walter.white@breakingbad.com\",\n" +
                "  \"phone\": \"+41787894502\"\n" +
                "}";
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL))
                .POST(BodyPublishers.ofString(newCustomer))
                .build();
        final HttpResponse<?> response = httpClient.send(request, BodyHandlers.discarding());
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void testgetOneByName() throws IOException, InterruptedException {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "?last_name=Fischer")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"last_name\":\"Fischer\"");
    }

    @Test
    void testGetAllCustomers() throws IOException, InterruptedException {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"last_name\":\"Fischer\"");
    }
}
