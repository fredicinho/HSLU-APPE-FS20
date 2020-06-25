package ch.hslu.appe.entities;

import org.junit.jupiter.api.Test;

public class JsonGeneratorTest {
    @Test
    public void testJsonGenerator(){
        String article = DemoValuesCreation.createDemoArticle();
        System.out.println(article);
    }
}
