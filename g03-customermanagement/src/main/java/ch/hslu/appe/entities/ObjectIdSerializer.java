package ch.hslu.appe.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectIdSerializer.class);

    /**
     * @param o the object id.
     * @param j the JSONGenerator.
     * @param s the serializer provider.
     * @throws IOException if there is an issue writing the object.
     */
    @Override
    public void serialize(final ObjectId o, final JsonGenerator j, final SerializerProvider s) throws IOException {
        if (o == null) {
            j.writeNull();
        } else {
            j.writeStartObject();
            j.writeObjectField("$oid", o.toString());
            j.writeEndObject();
        }
    }
}
