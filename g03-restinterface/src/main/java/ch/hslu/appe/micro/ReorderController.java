package ch.hslu.appe.micro;
import ch.hslu.appe.business.ReorderClientSync;
import io.micronaut.http.annotation.*;
import io.reactivex.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Controller("/api/v1/typeIn")
public class ReorderController {

    private static final Logger LOG = LoggerFactory.getLogger(ReorderController.class);
    private final ReorderClientSync reorderClientSync;

    public ReorderController(final ReorderClientSync reorderClientSync) {
        this.reorderClientSync = reorderClientSync;
    }


    /**
     * Creates a new Reorder.
     * @param reorder Reorder to do.
     * @return Created reorder.
     */
    @Post("/")
    public Boolean newReorder(@Body final String reorder) {
        LOG.info("REST, ReorderController, creating new reorder");
        return reorderClientSync.newReorder(reorder);
    }

    /**
     * Updates an existing Article in the Storage-Service.
     * @param reorderId reorder to update
     * @return True if Request was successfull
     */
    @Put("/")
    public Boolean setAsInserted(@Body final String reorderId) {
        reorderClientSync.setAsInserted(reorderId);
        LOG.info("REST, ReorderController, insert articles into warehouse: " + reorderId);
        return true;
    }


    /**
     * Get a StorageItem by reorderID.
     * @param reorderId ID of storageItem.
     * @return Gets all Items or just specified one.
     */
    @Get("/{?reorderId}")
    public List<String> getStorageItem(@Nullable final String reorderId) {
        if (reorderId.isEmpty()) {
            return this.reorderClientSync.getAllReorder(reorderId);
        } else {
            ArrayList<String> list = new ArrayList<>();
            LOG.warn(reorderId);
            list.add(this.reorderClientSync.getOneReorder(reorderId));
            return list;
        }
    }
}
