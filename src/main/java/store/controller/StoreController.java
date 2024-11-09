package store.controller;

import store.model.Catalog;
import store.model.Product;
import store.model.Promotion;
import store.service.CatalogService;
import store.service.InventoryService;
import store.service.parser.CsvParser;
import store.service.parser.ProductCsvParser;
import store.service.parser.PromotionCsvParser;
import store.view.FileLoader;
import store.view.ProductOutputView;

public class StoreController {

    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    private final FileLoader fileLoader = new FileLoader();
    private InventoryService inventoryService;

    private Catalog<Promotion> initPromotionCatalog() {
        CsvParser<Promotion> promotionCsvParser = new PromotionCsvParser();
        CatalogService<Promotion> promotionCatalogService = new CatalogService<>(promotionCsvParser);
        return promotionCatalogService.create(fileLoader.read(PROMOTION_FILE_PATH));
    }

    private Catalog<Product> initProductCatalog(Catalog<Promotion> promotionCatalog) {
        CsvParser<Product> productCsvParser = new ProductCsvParser(promotionCatalog);
        CatalogService<Product> productCatalogService = new CatalogService<>(productCsvParser);
        return productCatalogService.create(fileLoader.read(PRODUCT_FILE_PATH));
    }

    private void initInventory(Catalog<Product> productCatalog) {
        inventoryService = new InventoryService(productCatalog);
        inventoryService.storeAllProduct();
    }

    public void init() {
        Catalog<Promotion> promotionCatalog = initPromotionCatalog();
        Catalog<Product> productCatalog = initProductCatalog(promotionCatalog);
        initInventory(productCatalog);
    }

    public void run() {
        ProductOutputView productOutputView = new ProductOutputView();
        productOutputView.print(inventoryService.getInventoryStatus());
    }
}
