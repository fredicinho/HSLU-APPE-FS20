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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class OrderControllerTestIT {
    private static final String BASE_URL = "http://restinterface.appe-g03.el.eee.intern/api/v1/order/";


    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);

    @Test
    void testOrderGetOne() throws Exception {
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "?orderID=5ec78abe7cf70864120179ec")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"$oid\":\"5ec78abe7cf70864120179ec\"");
    }

    @Test
    void testOrderCreateOne() throws IOException, InterruptedException {
        final String newOrder = "{\"customerID\":\"2f55ccc2-48a4-4c43-8af3-44694cc4136a\",\"orderPositionList\":[{\"articleID\":\"765f42cc-c319-41c0-9af6-f7c78cfdff7d\",\"count\":3}]}";
        final HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL))
                .POST(BodyPublishers.ofString(newOrder))
                .build();
        final HttpResponse<?> response = httpClient.send(request, BodyHandlers.discarding());
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
