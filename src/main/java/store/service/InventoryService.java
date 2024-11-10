package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.ProductDto;
import store.model.Catalog;
import store.model.Inventory;
import store.model.Product;
import store.model.Promotion;

public class InventoryService {
    private final Catalog<Product> productCatalog;
    private final Inventory productInventory;
    private final Inventory promotionProductInventory;
    private final List<String> keySet;

    public InventoryService(Catalog<Product> productCatalog, Inventory productInventory,
                            Inventory promotionProductInventory) {
        this.productCatalog = productCatalog;
        this.productInventory = productInventory;
        this.promotionProductInventory = promotionProductInventory;
        keySet = productCatalog.getItems().stream().map(Product::getName).distinct().toList();
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

    private void createProductStatusOnlyPromotion(List<ProductDto> inventoryStatus, String key) {
        Product product = promotionProductInventory.search(key);
        inventoryStatus.add(new ProductDto(product.getName(), product.getPrice(), 0));
    }

    private void createProductStatus(List<ProductDto> inventoryStatus, String key) {
        if (!productInventory.hasProduct(key)) {
            createProductStatusOnlyPromotion(inventoryStatus, key);
            return;
        }
        Product product = productInventory.search(key);
        inventoryStatus.add(new ProductDto(product.getName(), product.getPrice(), product.getQuantity()));
    }

    private void createPromotionProductStatus(List<ProductDto> inventoryStatus, String key) {
        if (promotionProductInventory.hasProduct(key)) {
            Product product = promotionProductInventory.search(key);
            Promotion promotion = product.getPromotion().get();
            inventoryStatus.add(new ProductDto(product.getName(),
                    product.getPrice(),
                    product.getQuantity(),
                    promotion.getName()));
        }
    }

    public List<ProductDto> getCurrentInventoryStatus() {

        List<ProductDto> inventoryStatus = new ArrayList<>();
        for (String key : keySet) {
            createPromotionProductStatus(inventoryStatus, key);
            createProductStatus(inventoryStatus, key);
        }
        return inventoryStatus;
    }
}
