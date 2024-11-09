package store.service;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.DateTimes;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import store.dto.OrderProduct;
import store.model.Product;
import store.model.Promotion;
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

    private boolean promotion(List<PurchaseProduct> purchaseProducts, OrderProduct orderProduct) {
        Product product = inventoryService.searchPromotionProduct(orderProduct.getName());
        if (product == null || product.getPromotion().isEmpty()) {
            return false;
        }

        Promotion promotion = product.getPromotion().get();
        LocalDate now = DateTimes.now().toLocalDate();
        LocalDate start = promotion.getStartTime();
        LocalDate end = promotion.getEndTime();
        if (now.isBefore(start) || now.isAfter(end)) {
            return false;
        }

        int buy = promotion.getBuy();
        int free = promotion.getFree();
        int mod = orderProduct.getQuantity() % (buy + free);

        if ((buy + free - mod) == 1) {
            System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n", orderProduct.getName());
            if (Console.readLine().equals("Y")) {
                orderProduct.setQuantity(orderProduct.getQuantity() + 1);
            }
        }
        if (product.getQuantity() < orderProduct.getQuantity()) {
            NumberFormat formatter = NumberFormat.getInstance();
            int lack = orderProduct.getQuantity() - product.getQuantity() + product.getQuantity() % (buy + free);
            System.out.printf("현재 %s %s개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n",
                    orderProduct.getName(),
                    formatter.format(lack));
            if (Console.readLine().equals("Y")) {
                Product defaultProduct = inventoryService.searchProduct(orderProduct.getName());
                defaultProduct.validateUpdateQuantity(orderProduct.getQuantity() - product.getQuantity());
                purchaseProducts.add(
                        new PurchaseProduct(defaultProduct, orderProduct.getQuantity() - product.getQuantity()));
            }
            orderProduct.setQuantity(product.getQuantity());
        }
        purchaseProducts.add(new PurchaseProduct(product, orderProduct.getQuantity()));
        return true;
    }

    public PurchaseTransaction create(String purchaseLine) {
        List<OrderProduct> orderProducts = takeOrders(purchaseLine);
        List<PurchaseProduct> purchaseProducts = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            if (!promotion(purchaseProducts, orderProduct)) {
                Product product = inventoryService.searchProduct(orderProduct.getName());
                product.validateUpdateQuantity(orderProduct.getQuantity());
                purchaseProducts.add(new PurchaseProduct(product, orderProduct.getQuantity()));
            }
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
