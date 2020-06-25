package ch.hslu.appe.entities;

import ch.hslu.appe.business.ProductClientAsync;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LocalWarehouse implements ArticleHandling {


    private static final Logger LOG = LoggerFactory.getLogger(LocalWarehouse.class);
    private int ID;
    private String name;
    private static LocalWarehouse instance;
    private static ProductClientAsync productClientAsync;

    private LocalWarehouse(ProductClientAsync productClientAsync) { this.productClientAsync = productClientAsync; }
    private static Gson gson = new Gson();

    public static LocalWarehouse getInstance () {
        if (LocalWarehouse.instance == null) {
            LocalWarehouse.instance = new LocalWarehouse(productClientAsync);
        }
        return LocalWarehouse.instance;
    }

    public void init(int ID, String name){
        this.ID = ID;
        this.name = name;
        MongoDB.setUpConnection(name);
    }


    @Override
    public Boolean addArticleToStock(String articleAsJson) {
        MongoDB.insertDocument(MongoDB.jsonToDocument(articleAsJson));
        return true;
    }

    @Override
    public String getArticleFromStock(String id) {
        Document article = MongoDB.getArticle(id);
        if(article == null){
            return "Article not found";
        }else{
            return article.toJson();
        }
    }

    @Override
    public List <String> getAllArticlesFromStock() {
        List<Document> doc = MongoDB.getAllArticles();
        List <String> string = new ArrayList <>();
        System.out.println(doc.size());
        for(int i=0; i < doc.size(); i++){
            string.add(doc.get(i).toJson());
        }
        return string;
    }

    @Override
    public Boolean updateArticleInStock(String json) {
        Article article = gson.fromJson(json, Article.class);
        return updateArticleInStock(article.getUniqueID(), json);
    }

    //Following methods are returning always true
    @Override
    public Boolean updateArticleInStock(String id, String article) {
        MongoDB.updateDocument(MongoDB.jsonToDocument(article));
        return true;
    }

    @Override
    public Boolean updateNumberInStock(String id, Integer number) {
        Article article = gson.fromJson(MongoDB.getArticle(id).toJson(), Article.class);
        article.setNumberInStock(article.getNumberInStock()-number);
        MongoDB.updateDocument(MongoDB.jsonToDocument(gson.toJson(article)));
        return true;
    }

    /**
     * Inserts price of every Orderposition into List.
     * @param orderInJson OrderAvailabilityRequest object as Json
     * @return updated with prices OrderAvailabilityRequest object as Json
     */
    public String processArticleWithPrice(final String orderInJson) {
        LOG.debug("Starting to get Prices of all articles in Orderrequest!");
        ObjectMapper mapper = new ObjectMapper();
        OrderAvailabilityRequest request = null;
        try {
            request = mapper.readValue(orderInJson, OrderAvailabilityRequest.class);
            request.getOrderPositionList().stream().forEach(position -> {
                Article articleFromDB = gson.fromJson(this.getArticleFromStock(position.getArticleID()), Article.class);
                position.setPrice(articleFromDB.getPrice());
                    });

        } catch (JsonProcessingException e) {
            LOG.info("Problems with parcing received JSON to OrderAvalabilityRequest-Object.");
        }
        String updatedOrderRequest = null;
        try {
            updatedOrderRequest = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return updatedOrderRequest;

    }

    /**
     * Handles an OrderRequest. Checks if there are enough articles in the Warehouse.
     * If all articles are available, NumberOfStock will get decremented.
     * If just one article is not available, the whole request will get rejected.
     * @param orderInJson List with all articles which should get processed.
     * @return String updated Order as Json
     */
    public String processArticleAvailability(final String orderInJson) {
        LOG.debug("Starting Update of Article Availability");
        ObjectMapper mapper = new ObjectMapper();
        OrderAvailabilityRequest request = null;
        try {
            request = mapper.readValue(orderInJson, OrderAvailabilityRequest.class);
            request.getOrderPositionList().stream().forEach(position -> {
                if (!(position.getAvailable())){
                    Article articleFromDB = gson.fromJson(this.getArticleFromStock(position.getArticleID()), Article.class);
                    if ( articleFromDB.getNumberInStock() > position.getCount()) {
                        position.setAvailable(true);
                        articleFromDB.setNumberInStock(articleFromDB.getNumberInStock()-position.getCount());
                        String updatedArticle = gson.toJson(articleFromDB);
                        this.updateArticleInStock(updatedArticle);
                        System.out.println("Database got updated with numbers of stock of article: " + updatedArticle);
                    } else {
                        productClientAsync.sendReorderRequest(articleFromDB.getUniqueID());
                    }
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String updatedOrderRequest = null;
        try {
            updatedOrderRequest = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return updatedOrderRequest;
    }

    @Override
    public int getNumberInStock(int id) {
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
}
