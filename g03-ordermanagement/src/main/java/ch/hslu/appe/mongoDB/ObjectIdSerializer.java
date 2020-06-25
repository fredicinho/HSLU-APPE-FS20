package ch.hslu.appe.mongoDB;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bson.types.ObjectId;

import java.io.IOException;

public final class ObjectIdSerializer extends JsonSerializer<ObjectId> {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectIdSerializer.class);

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
