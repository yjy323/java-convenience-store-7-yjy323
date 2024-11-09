package store;

import store.model.Catalog;
import store.model.Product;
import store.model.Promotion;
import store.service.CatalogService;
import store.service.CsvParser;
import store.service.ProductCsvParser;
import store.service.PromotionCsvParser;
import store.view.FileLoader;

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
    }
}
