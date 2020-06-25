package ch.hslu.appe.micro;
import ch.hslu.appe.entities.Entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import java.io.IOException;

/**
 * A Interface which defines a Service for an Entity to persist an Entity on the Database in the OrderManagement.
 * @param <T> Entity which has to be implemented to use this Service.
 */
public interface ServiceType<T extends Entity> {
    /**
     * Method to change to the right collection of the used Entity.
     */
    void changeCollection();
    /**
     * Method which should be used whenever interacting with the MongoDbAdapter.
     */
    void checkCollection();
    /**
     * Needs to be declared to use the default-Converters.
     * @return Type of the used Entity in this Service.
     */
    Class<T> getType();
    /**
     * Converts Object to Document.
     * @param obj Object to parse
     * @return Document
     * @throws JsonProcessingException if convert fails
     */
    default Document convert(T obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return Document.parse(mapper.writeValueAsString(obj));
    }
    /**
     * Converts Document to Order. Document gets converted to JSON and then to Order.
     * @param doc Document to parse
     * @return Order which was passed as Document
     * @throws IOException if convert fails
     */
    default T convert(Document doc) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(doc.toJson(), getType());
    }
}
