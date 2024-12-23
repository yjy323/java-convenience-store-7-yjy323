package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.ProductDto;
import store.model.Catalog;
import store.model.Inventory;
import store.model.Product;
import store.model.Promotion;

public class InventoryService {
    private final Inventory productInventory;
    private final Inventory promotionProductInventory;

    public InventoryService(Inventory productInventory,
                            Inventory promotionProductInventory) {
        this.productInventory = productInventory;
        this.promotionProductInventory = promotionProductInventory;
    }

    private void storeProductOnlyPromotionProduct(Catalog<Product> productCatalog) {
        for (Product product : productCatalog.getItems()) {
            String productName = product.getName();
            if (promotionProductInventory.hasProduct(productName) && !productInventory.hasProduct(productName)) {
                productInventory.store(new Product(productName, product.getPrice(), 0, null));
            }
        }
    }

    public void storeAllProduct(Catalog<Product> productCatalog) {
        for (Product product : productCatalog.getItems()) {
            if (product.getPromotion().isPresent()) {
                promotionProductInventory.store(product);
                continue;
            }
            productInventory.store(product);
        }
        storeProductOnlyPromotionProduct(productCatalog);
    }

    private void createProductStatus(List<ProductDto> inventoryStatus, String key) {
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

    public List<ProductDto> getCurrentInventoryStatus(List<String> keySet) {

        List<ProductDto> inventoryStatus = new ArrayList<>();
        for (String key : keySet) {
            createPromotionProductStatus(inventoryStatus, key);
            createProductStatus(inventoryStatus, key);
        }
        return inventoryStatus;
    }
}
