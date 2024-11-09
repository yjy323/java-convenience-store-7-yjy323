package store.controller;

import camp.nextstep.edu.missionutils.Console;
import store.model.Catalog;
import store.model.Product;
import store.model.Promotion;
import store.model.PurchaseTransaction;
import store.service.CatalogService;
import store.service.InventoryService;
import store.service.PurchaseService;
import store.service.parser.CsvParser;
import store.service.parser.ProductCsvParser;
import store.service.parser.PromotionCsvParser;
import store.view.FileLoader;
import store.view.ProductOutputView;
import store.view.PurchaseInputView;

public class StoreController {

    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    private final FileLoader fileLoader = new FileLoader();
    private InventoryService inventoryService;

    /*
     * Init Method
     * */

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

    /*
     * Run Method
     * */

    private void welcome(ProductOutputView productOutputView) {
        productOutputView.print(inventoryService.getInventoryStatus());
    }

    private void purchase(PurchaseInputView purchaseInputView) {
        PurchaseService purchaseService = new PurchaseService(inventoryService);
        while (true) {
            try {
                PurchaseTransaction transaction = purchaseService.create(purchaseInputView.read());
                purchaseService.purchase(transaction);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void run() {
        boolean status = true;
        while (status) {
            welcome(new ProductOutputView());
            purchase(new PurchaseInputView());
            System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
            status = Console.readLine().equals("Y");
        }
    }
}
