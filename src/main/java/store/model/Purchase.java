package store.model;

import java.util.Collections;
import java.util.List;

public class Purchase {
    private final List<PurchaseProduct> items;

    public Purchase(List<PurchaseProduct> items) {
        this.items = items;
    }

    public List<PurchaseProduct> getItems() {
        return Collections.unmodifiableList(items);
    }
}
