package ch.hslu.appe.business;

import io.micronaut.configuration.rabbitmq.annotation.Binding;
import io.micronaut.configuration.rabbitmq.annotation.RabbitClient;
import io.micronaut.configuration.rabbitmq.annotation.RabbitProperty;
import java.util.List;



@RabbitClient("g03")
@RabbitProperty(name = "replyTo", value = "amq.rabbitmq.reply-to")
public interface StorageClientSync {

    /**
     * Methode welche beim StorageService einen Artikel im JSON-Format beantragt.
     * @param requestItem StorageList request
     * @return Returns Item as JSON
     */
    @Binding("storage.one")
    String getOneArticle(String requestItem);

    @Binding("storage.all")
    List<String> getAllArticles(String storageId);

    @Binding("storage.insert")
    Boolean insertArticle(String article);

    @Binding("storage.updateWhole")
    Boolean updateArticle(String articleNew);

    @Binding("storage.updateNumber")
    Boolean updateArticleNumber(String id, Integer numberToAdd);
}

