package store.model;

import java.util.Collections;
import java.util.List;

public class PurchaseTransaction {
    private final List<PurchaseProduct> items;

    public PurchaseTransaction(List<PurchaseProduct> items) {
        this.items = items;
    }

    public List<PurchaseProduct> getItems() {
        return Collections.unmodifiableList(items);
    }
}
