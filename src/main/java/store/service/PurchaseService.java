package store.service;

import java.util.ArrayList;
import java.util.List;
import store.dto.OrderProduct;
import store.model.Product;
import store.model.PurchaseProduct;
import store.model.PurchaseTransaction;
import store.service.parser.StringParser;

public class PurchaseService {

    private final InventoryService inventoryService;

    public PurchaseService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    private List<OrderProduct> takeOrders(String orderLine) {
        List<String> rawOrder = List.of(orderLine.split(","));
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (String data : rawOrder) {
            orderProducts.add(StringParser.parseOrder(data));
        }
        return orderProducts;
    }

    public PurchaseTransaction create(String purchaseLine) {
        List<OrderProduct> orderProducts = takeOrders(purchaseLine);
        List<PurchaseProduct> purchaseProducts = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            Product product = inventoryService.searchProduct(orderProduct.getName());
            product.validateUpdateQuantity(orderProduct.getQuantity());
            purchaseProducts.add(new PurchaseProduct(product, orderProduct.getQuantity()));
        }
        return new PurchaseTransaction(purchaseProducts);
    }

    private void purchaseProduct(PurchaseProduct purchaseProduct) {
        purchaseProduct.getProduct().buy(purchaseProduct.getQuantity());
    }

    public void purchase(PurchaseTransaction transaction) {
        for (PurchaseProduct purchaseProduct : transaction.getItems()) {
            purchaseProduct(purchaseProduct);
        }
    }
}
