package ch.hslu.appe.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.util.*;

public class DemoValuesCreation {
    private static Gson gson = new Gson();
    private static Random rand = new Random();
    private static ObjectMapper mapper = new ObjectMapper();
    private static String _id;
    @JsonProperty
    private static Map <String, String> mapFromArticleJson = new HashMap <>();

    private static Category getRandomCategory(){
        List <Category> categoryList = new ArrayList();
        categoryList.add(new Category(1, "IT"));
        categoryList.add(new Category(2, "Keyboards"));
        categoryList.add(new Category(3, "Chair"));

        return categoryList.get(rand.nextInt(2));
    }

    public static String createDemoArticle(){
        Article demoArticle = new Article("demoArticle"+rand.nextInt(10000), rand.nextInt(1000), rand.nextInt(700), getRandomCategory());
        _id = demoArticle.getUniqueID();
        return gson.toJson(demoArticle);
    }

    public static String createSpecificDemoArticle(final String name, final int numberInStock, final double price) {
        Article demoArticle = new Article(name, numberInStock, price, getRandomCategory());
        return gson.toJson(demoArticle);
    }

    public static String getUUID() {
        return _id;
    }



}
