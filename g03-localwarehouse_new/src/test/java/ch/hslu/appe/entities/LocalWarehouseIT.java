package ch.hslu.appe.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalWarehouseIT {

    private static ObjectMapper mapper = new ObjectMapper();
    private static Gson gson = new Gson();

    @Test
    void testGetFromDatabase() throws JsonProcessingException {
        LocalWarehouse localWarehouse = LocalWarehouse.getInstance();
        localWarehouse.init(0, "testReadOne");
        String demoArticle = DemoValuesCreation.createDemoArticle();
        localWarehouse.addArticleToStock(demoArticle);
        //System.out.println(DemoValuesCreation.getUUID());
        assertThat(demoArticle.equals( localWarehouse.getArticleFromStock(DemoValuesCreation.getUUID())));
    }
    @Test
    void testGetAllFromDatabase(){

        LocalWarehouse localWarehouse = LocalWarehouse.getInstance();
        localWarehouse.init(4, "randomInit");
        MongoDB.deleteCollection(localWarehouse.getName());
        for(int i = 0; i<10; i++){
            localWarehouse.addArticleToStock(DemoValuesCreation.createDemoArticle());
        }
        assertEquals(10, localWarehouse.getAllArticlesFromStock().size());
    }

    @Test
    void testUpdateOneArticle(){
        LocalWarehouse localWarehouse = LocalWarehouse.getInstance();
        localWarehouse.init(4, "randomInit");
        MongoDB.deleteCollection(localWarehouse.getName());
        String demoArticle = DemoValuesCreation.createDemoArticle();
        String UUID = DemoValuesCreation.getUUID();
        localWarehouse.addArticleToStock(demoArticle);
        System.out.println(demoArticle);
        Article article = gson.fromJson(demoArticle, Article.class);
        article.setName("Crap");
        article.setCategory(new Category(3, "Muster"));
        article.setNumberInStock(333);
        String updatedArticleJson;
        updatedArticleJson = gson.toJson(article);
        localWarehouse.updateArticleInStock(UUID, updatedArticleJson);
        Article updatedFromDB = gson.fromJson(localWarehouse.getArticleFromStock(UUID), Article.class);
        assertTrue(updatedFromDB.toString().equals(article.toString()));
    }

    @Test
    void testUpdateDecreasingNumberInStock(){
        LocalWarehouse localWarehouse = LocalWarehouse.getInstance();
        localWarehouse.init(4, "randomInit");
        MongoDB.deleteCollection(localWarehouse.getName());
        String demoArticle = DemoValuesCreation.createDemoArticle();
        Article article = gson.fromJson(demoArticle, Article.class);
        article.setNumberInStock(article.getNumberInStock() -22);
        String UUID = DemoValuesCreation.getUUID();
        localWarehouse.addArticleToStock(demoArticle);
        localWarehouse.updateNumberInStock(UUID, 22);
        Article updatedFromDB = gson.fromJson(localWarehouse.getArticleFromStock(UUID), Article.class);
        assertTrue(updatedFromDB.getNumberInStock() == article.getNumberInStock());
    }



}
