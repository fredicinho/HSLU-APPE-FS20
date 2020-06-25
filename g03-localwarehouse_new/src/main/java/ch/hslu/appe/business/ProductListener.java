package ch.hslu.appe.business;

import ch.hslu.appe.entities.LocalWarehouse;
import io.micronaut.configuration.rabbitmq.annotation.Queue;
import io.micronaut.configuration.rabbitmq.annotation.RabbitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

//@Requires(property = "spec.name", value = "RpcUppercaseSpec")
// tag::clazz[]
@RabbitListener
public class ProductListener {

    LocalWarehouse localWarehouse = LocalWarehouse.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(ProductListener.class);
    private final ProductClientAsync productClientAsync;

    public ProductListener(ProductClientAsync productClientAsync) {
        this.productClientAsync = productClientAsync;
    }

    @Queue("localWarehouseOne")
    public String getArticleById(String id) {
        return localWarehouse.getArticleFromStock(id);
    }

    @Queue("localWarehouseAll")
    public List <String> getAllArticles() {
        System.out.println(localWarehouse.getName());
        return localWarehouse.getAllArticlesFromStock();
    }

    @Queue("localWarehouseInsert")
    public Boolean insertIntoDB(String json){
        return localWarehouse.addArticleToStock(json);
    }

    @Queue("localWarehouseUpdate")
    public boolean update(String json){
        return localWarehouse.updateArticleInStock(json);
    }

    @Queue("localWarehouseUpdateNumber")
    public boolean updateNumberInStock(String id, Integer number){
        return localWarehouse.updateNumberInStock(id, number);
    }

    @Queue("localWarehouseOrderRequest")
    public void orderrequest(String json) {
        String answer = localWarehouse.processArticleAvailability(json);
        LOG.info("Processed Order looks like that now: " + answer);
        this.productClientAsync.sendOrderBack(answer);
        LOG.info("Sended Order back.");
    }

    @Queue("localWarehouseOrderGetPrice")
    public String getPriceOfOrderRequestArticles(String json) {
        String answer = localWarehouse.processArticleWithPrice(json);
        LOG.info("Processed Order looks like that now: " + answer);
        return answer;
    }


}
