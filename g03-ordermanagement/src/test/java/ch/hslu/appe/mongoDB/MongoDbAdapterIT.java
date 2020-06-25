package ch.hslu.appe.mongoDB;

import ch.hslu.appe.entities.Order;
import ch.hslu.appe.entities.OrderPosition;
import ch.hslu.appe.entities.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MongoDbAdapterIT {

    @Test
    public void testSingleton() {
        MongoDbAdapter adapter = MongoDbAdapter.getInstance();
        assertEquals(adapter, MongoDbAdapter.getInstance());
    }

    @Test
    public void testChangeCollection() {
        MongoDbAdapter adapter = MongoDbAdapter.getInstance();
        adapter.changeCollection("db", "collection");
        assertEquals("db", adapter.getDatabaseName());
        assertEquals("collection", adapter.getCollectionName());
    }

    @Test
    public void testCreateAndGetOne() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        MongoDbAdapter adapter = MongoDbAdapter.getInstance();
        adapter.changeCollection("testdb", "testcollection");
        ObjectId id = new ObjectId();
        Order testOrder = new Order(id, new Date(), State.DELIVERED, "1", Arrays.asList(new OrderPosition("123", 3)));
        adapter.create(Document.parse(new ObjectMapper().writeValueAsString(testOrder)));
        Document result = adapter.getOneByID(id.toString());
        Order orderFound = mapper.readValue(result.toJson(), Order.class);
        assertEquals(testOrder.getId(), orderFound.getId());
    }

}