package ch.hslu.appe.business;

import io.micronaut.configuration.rabbitmq.annotation.Binding;
import io.micronaut.configuration.rabbitmq.annotation.RabbitClient;
import io.micronaut.configuration.rabbitmq.annotation.RabbitProperty;

import java.util.List;

@RabbitClient("g03")
@RabbitProperty(name = "replyTo", value = "amq.rabbitmq.reply-to")

public interface ReorderClientSync {

    /**
     * Methode welche beim StorageService einen Artikel im JSON-Format beantragt.
     * @param reorder whole reorder as json
     * @return Returns Item as JSON
     */
    @Binding("reorder.insert")
    Boolean newReorder(String reorder);

    @Binding("reorder.getOne")
    String getOneReorder(String reorderId);

    @Binding("reorder.getAll")
    List<String> getAllReorder(String reorderId);

    @Binding("reorder.update")
    Boolean setAsInserted(String reorderId);


}
