package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.ProductStatus;
import store.model.Catalog;
import store.model.Inventory;
import store.model.Product;
import store.model.Promotion;

public class InventoryService {
    private final Inventory productInventory = new Inventory();
    private final Inventory promotionProductInventory = new Inventory();
    private final Catalog<Product> productCatalog;

    public InventoryService(Catalog<Product> productCatalog) {
        this.productCatalog = productCatalog;
    }

    public void storeAllProduct() {
        for (Product product : productCatalog.getItems()) {
            if (product.getPromotion().isPresent()) {
                promotionProductInventory.store(product);
                continue;
            }
            productInventory.store(product);
        }
    }

    private void createProductStatusOnlyPromotion(List<ProductStatus> inventoryStatus, String key) {
        Product product = promotionProductInventory.search(key).get();
        inventoryStatus.add(new ProductStatus(product.getName(), product.getPrice(), 0));
    }

    private void createProductStatus(List<ProductStatus> inventoryStatus, String key) {
        if (productInventory.search(key).isEmpty()) {
            createProductStatusOnlyPromotion(inventoryStatus, key);
            return;
        }
        Product product = productInventory.search(key).get();
        inventoryStatus.add(new ProductStatus(product.getName(), product.getPrice(), product.getQuantity()));
    }

    private void createPromotionProductStatus(List<ProductStatus> inventoryStatus, String key) {
        if (promotionProductInventory.search(key).isPresent()) {
            Product product = promotionProductInventory.search(key).get();
            Promotion promotion = product.getPromotion().get();
            inventoryStatus.add(new ProductStatus(product.getName(),
                    product.getPrice(),
                    product.getQuantity(),
                    promotion.getName()));
        }
    }

    public List<ProductStatus> getInventoryStatus() {
        List<String> keySet = productCatalog.getItems().stream().map(Product::getName).distinct().toList();

        List<ProductStatus> inventoryStatus = new ArrayList<>();
        for (String key : keySet) {
            createPromotionProductStatus(inventoryStatus, key);
            createProductStatus(inventoryStatus, key);
        }
        return inventoryStatus;
    }
}
