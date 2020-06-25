package ch.hslu.appe.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {

    /**
     * Desrializes JSON.
     * @param p a JSON parser.
     * @param ctxt the deserialization context.
     * @return the object id.
     * @throws IOException if there is a problem reading the id.
     */
    @Override
    public ObjectId deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        JsonNode oid = ((JsonNode) p.readValueAsTree()).get("$oid");
        return new ObjectId(oid.asText());
    }
}
