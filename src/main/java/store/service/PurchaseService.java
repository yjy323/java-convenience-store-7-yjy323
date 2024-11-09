package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import store.dto.OrderProduct;
import store.model.Product;
import store.model.Promotion;
import store.model.PurchaseProduct;
import store.model.PurchaseTransaction;
import store.service.parser.StringParser;
import store.view.PromotionInputView;

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

    private boolean isPromotionProductOrder(OrderProduct orderProduct) {
        Product product;
        try {
            product = inventoryService.searchPromotionProduct(orderProduct.getName());
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (product.getPromotion().isEmpty()) {
            return false;
        }
        return product.getPromotion().get().isPromotionPeriod(DateTimes.now().toLocalDate());
    }

    private void purchasePromotionProduct(List<PurchaseProduct> purchaseProducts, OrderProduct orderProduct) {

        PromotionInputView promotionInputView = new PromotionInputView();
        Product product = inventoryService.searchPromotionProduct(orderProduct.getName());
        Promotion promotion = product.getPromotion().get();

        int buy = promotion.getBuy();
        int free = promotion.getFree();
        int mod = orderProduct.getQuantity() % (buy + free);
        if ((buy + free - mod) == 1) {
            if (promotionInputView.confirmAdditionalFree(orderProduct.getName())) {
                orderProduct.setQuantity(orderProduct.getQuantity() + 1);
            }
        }

        if (product.getQuantity() < orderProduct.getQuantity()) {
            int lack = orderProduct.getQuantity() - product.getQuantity() + product.getQuantity() % (buy + free);
            if (promotionInputView.confirmPurchaseWithoutPromotion(orderProduct.getName(), lack)) {
                purchaseProduct(purchaseProducts,
                        new OrderProduct(orderProduct.getName(), orderProduct.getQuantity() - product.getQuantity()));
            }
            orderProduct.setQuantity(product.getQuantity());
        }

        purchaseProducts.add(new PurchaseProduct(product, orderProduct.getQuantity()));
    }

    private void purchaseProduct(List<PurchaseProduct> purchaseProducts, OrderProduct orderProduct) {
        Product product = inventoryService.searchProduct(orderProduct.getName());
        product.validateUpdateQuantity(orderProduct.getQuantity());
        purchaseProducts.add(new PurchaseProduct(product, orderProduct.getQuantity()));
    }

    public PurchaseTransaction createTransaction(String purchaseLine) {
        List<OrderProduct> orderProducts = takeOrders(purchaseLine);
        List<PurchaseProduct> purchaseProducts = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            if (isPromotionProductOrder(orderProduct)) {
                purchasePromotionProduct(purchaseProducts, orderProduct);
                continue;
            }
            purchaseProduct(purchaseProducts, orderProduct);
        }
        return new PurchaseTransaction(purchaseProducts);
    }
}
