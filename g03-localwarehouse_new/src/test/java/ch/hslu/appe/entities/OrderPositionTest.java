package ch.hslu.appe.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderPositionTest {

    @Test
    public void mapperOfOrderPositionsTest() throws JsonProcessingException {
        String json = "[{\"articleID\":\"1\",\"count\":1}, {\"articleID\":\"2\",\"count\":2}]";
        ObjectMapper mapper = new ObjectMapper();
        List<OrderPosition> positions = Arrays.asList(mapper.readValue(json, OrderPosition[].class));
        for (OrderPosition position : positions) {
            System.out.println(position.toString());
        }
        assertEquals(1, positions.get(0).getCount());
        assertEquals("1", positions.get(0).getArticleID());
        assertEquals(2, positions.get(1).getCount());
        assertEquals("2", positions.get(1).getArticleID());
    }
}
