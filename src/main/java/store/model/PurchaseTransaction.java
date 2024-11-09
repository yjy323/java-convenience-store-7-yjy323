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

    private void purchaseProduct(PurchaseProduct purchaseProduct) {
        purchaseProduct.getProduct().buy(purchaseProduct.getQuantity());
    }

    public void purchase() {
        for (PurchaseProduct purchaseProduct : items) {
            purchaseProduct(purchaseProduct);
        }
    }
}
