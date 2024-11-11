package store.controller;

import java.util.List;
import store.model.Catalog;
import store.model.Inventory;
import store.model.Payment;
import store.model.Product;
import store.model.Promotion;
import store.service.CatalogService;
import store.service.InventoryService;
import store.service.PaymentService;
import store.service.PurchaseService;
import store.service.parser.CsvParser;
import store.service.parser.ProductCsvParser;
import store.service.parser.PromotionCsvParser;
import store.view.FileLoader;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";

    private final InputView inputView;
    private final OutputView outputView;
    private final FileLoader fileLoader;

    private final Inventory productInventory = new Inventory();
    private final Inventory promotionProductInventory = new Inventory();
    private List<String> keySet;

    private InventoryService inventoryService;
    private Payment payment;


    public StoreController(FileLoader fileLoader, InputView inputView, OutputView outputView) {
        this.fileLoader = fileLoader;
        this.inputView = inputView;
        this.outputView = outputView;
    }

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
        keySet = productCatalog.getKeySet();
        inventoryService = new InventoryService(productInventory, promotionProductInventory);
        inventoryService.storeAllProduct(productCatalog);
    }

    public void init() {
        Catalog<Promotion> promotionCatalog = initPromotionCatalog();
        Catalog<Product> productCatalog = initProductCatalog(promotionCatalog);
        initInventory(productCatalog);
    }

    /*
     * Run Method
     * */

    private void welcome() {
        outputView.printProducts(inventoryService.getCurrentInventoryStatus(keySet));
    }

    private void purchase() {
        PurchaseService purchaseService = new PurchaseService(inputView, productInventory, promotionProductInventory);
        while (true) {
            try {
                payment = purchaseService.purchaseProcess();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void payment() {
        PaymentService paymentService = new PaymentService(payment);
        paymentService.confirmMembership(inputView.confirmMembership());
        outputView.printReceipt(paymentService.paymentProcess());
    }

    public void run() {
        boolean status = true;
        while (status) {
            welcome();
            purchase();
            payment();

            status = inputView.confirmContinuePurchase();
        }
    }
}
