package ch.hslu.appe.entities;

import java.util.List;

/**
 * @author oliverwerlen
 * @version 1.0
 *
 */

public interface ArticleHandling {
    Boolean addArticleToStock(String string);
    String getArticleFromStock(String id);
    List <String> getAllArticlesFromStock();
    Boolean updateArticleInStock(String article);
    Boolean updateArticleInStock(String id, String article);
    Boolean updateNumberInStock(String id, Integer number);
    int getNumberInStock(int id);
}
