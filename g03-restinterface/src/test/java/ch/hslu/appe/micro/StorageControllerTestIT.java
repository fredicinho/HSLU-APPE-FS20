package ch.hslu.appe.micro;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class StorageControllerTestIT {
    private static final String BASE_URL = "http://restinterface.appe-g03.el.eee.intern/api/v1/storage";

    @Test
    void testGetStorageItemOne() throws IOException, InterruptedException {
        final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        final HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "?storageItemID=765f42cc-c319-41c0-9af6-f7c78cfdff7d")).GET().build();
        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\\\"_id\\\" : \\\"765f42cc-c319-41c0-9af6-f7c78cfdff7d\\\"");
        assertThat(response.body()).contains("\\\"name\\\" : \\\"demoArticle7056\\\"");
    }
}
