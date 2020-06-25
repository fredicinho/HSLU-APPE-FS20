package ch.hslu.appe.micro;

import ch.hslu.appe.business.StorageClientSync;
import io.micronaut.http.annotation.*;
import io.reactivex.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

@Controller("/api/v1/storage")
public class StorageController {

    private static final Logger LOG = LoggerFactory.getLogger(StorageController.class);
    private final StorageClientSync storageClientSync;



    public StorageController(final StorageClientSync storageClientSync) {
        this.storageClientSync = storageClientSync;
    }

    /**
     * Methode welche beim StorageService ein Artikel mit der storageItemID im JSON-Format beantragt.
     * @param article Article to add in localwarehouse.
     * @return Gefundener Artikel im JSON format und falls nicht vorhanden wird eine Liste aller Artiel zurückgegeben.
     */
    @Post("/")
    public Boolean addNewArticle(@Body final String article) {
        LOG.info("REST, StorageController, inserting new article");
        return storageClientSync.insertArticle(article);
    }

    /**
     * Updates an existing Article in the Storage-Service.
     * @param storageItemID Article to update
     * @param numberToAdd numbers to add to number in Stock.
     * @param articleNew new Article witch will be added if no existed article will be updated.
     */
    @Put("/{?storageItemID}")
    public void updateExisting(@Nullable final String storageItemID, @Body @Nullable final Integer numberToAdd,
                               @Body @Nullable final String articleNew) {
        LOG.info("REST, StorageController, update item");
        if (numberToAdd == null && storageItemID.isEmpty()) {
            storageClientSync.updateArticle(articleNew);
            LOG.info("REST, StorageController, updating whole article");
        } else {
            storageClientSync.updateArticleNumber(storageItemID, numberToAdd);
            LOG.info("REST, StorageController, updating number in stock from article");
        }
    }


    /**
     * Gets an article from the localwarehouse by given id. Else returns all articles.
     * @param storageItemID ID of article to get.
     * @return Article as JSON or List with articles.
     */
    @Get("/{?storageItemID}")
    public List<String> getStorageItem(@Nullable final String storageItemID) {
        if (storageItemID.isEmpty()) {
            return this.storageClientSync.getAllArticles(storageItemID);
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(this.storageClientSync.getOneArticle(storageItemID));
            return list;
        }
    }

}
