package store.service;

import store.model.Catalog;
import store.model.Inventory;
import store.model.Product;

public class InventoryService {
    private final Inventory productInventory = new Inventory();
    private final Inventory promotionProductInventory = new Inventory();

    private final Catalog<Product> productCatalog;

    public InventoryService(Catalog<Product> productCatalog) {
        this.productCatalog = productCatalog;
    }

    private boolean isPromotionProduct(Product product) {
        if (product.getPromotion().isPresent()) {
            return true;
        }
        return false;
    }

    public void storeAllProduct() {
        for (Product product : productCatalog.getItems()) {
            if (isPromotionProduct(product)) {
                promotionProductInventory.store(product);
                continue;
            }
            productInventory.store(product);
        }
    }
}
