package ch.hslu.appe.business;

import java.util.ArrayList;
import java.util.List;

public class IncompleteDataException extends RuntimeException {

    private List<String> missingFields = new ArrayList<>();
    private static final long serialVersionUID = 3294857293484733234L;

    public IncompleteDataException(final List<String> missingFields) {
        this.missingFields = missingFields;
    }

    /**
     * Provides a list of customer attributes that have not been provided by front end.
     * @return a list of attribute names which are missing values.
     */
    public List<String> getMissingFields() {
        return missingFields;
    }
}
