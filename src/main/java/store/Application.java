package store;

import store.model.Catalog;
import store.model.Product;
import store.model.Promotion;
import store.service.CatalogService;
import store.service.InventoryService;
import store.service.parser.CsvParser;
import store.service.parser.ProductCsvParser;
import store.service.parser.PromotionCsvParser;
import store.view.FileLoader;
import store.view.StoreInfoOutputView;

public class Application {

    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    public static void main(String[] args) {
        FileLoader fileLoader = new FileLoader();

        CsvParser<Promotion> promotionCsvParser = new PromotionCsvParser();
        CatalogService<Promotion> promotionCatalogService = new CatalogService<>(promotionCsvParser);
        Catalog<Promotion> promotionCatalog = promotionCatalogService.create(fileLoader.read(PROMOTION_FILE_PATH));

        CsvParser<Product> productCsvParser = new ProductCsvParser(promotionCatalog);
        CatalogService<Product> productCatalogService = new CatalogService<>(productCsvParser);
        Catalog<Product> productCatalog = productCatalogService.create(fileLoader.read(PRODUCT_FILE_PATH));

        InventoryService inventoryService = new InventoryService(productCatalog);
        inventoryService.storeAllProduct();

        StoreInfoOutputView storeInfoOutputView = new StoreInfoOutputView();
        storeInfoOutputView.print(inventoryService.getInventoryStatus());
    }
}
