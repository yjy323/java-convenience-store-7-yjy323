package store.service;

import store.dto.ProductStatus;
import store.dto.Receipt;
import store.model.Product;
import store.model.Promotion;
import store.model.PurchaseProduct;
import store.model.PurchaseTransaction;

public class PaymentService {

    private final PurchaseTransaction purchaseTransaction;
    private boolean membershipStatus = false;

    public PaymentService(PurchaseTransaction purchaseTransaction) {
        this.purchaseTransaction = purchaseTransaction;
    }

    public void confirmMembership(boolean confirm) {
        if (confirm) {
            membershipStatus = true;
        }
    }

    private ProductStatus createProductStatus(Product product) {
        return new ProductStatus(product.getName(), product.getPrice(), product.getQuantity());
    }

    public Receipt createReceipt() {
        Receipt receipt = new Receipt();
        receipt.setMembershipStatus(membershipStatus);
        for (PurchaseProduct purchaseProduct : purchaseTransaction.getItems()) {
            Product product = purchaseProduct.getProduct();
            receipt.addTotalQuantity(purchaseProduct.getQuantity());
            if (product.getPromotion().isEmpty()) {
                receipt.addProducts(
                        new ProductStatus(product.getName(), product.getPrice(), purchaseProduct.getQuantity()));
                receipt.addRegularTotalPrice(purchaseProduct.getQuantity() * product.getPrice());
            } else {
                Promotion promotion = product.getPromotion().get();
                int buyFree = promotion.getBuy() + promotion.getFree();
                int freeQuantity = purchaseProduct.getQuantity() / buyFree;
                receipt.addProducts(
                        new ProductStatus(product.getName(), product.getPrice(), purchaseProduct.getQuantity()));
                receipt.addFreeProducts(new ProductStatus(product.getName(), product.getPrice(), freeQuantity));
                receipt.addPromotionTotalPrice(purchaseProduct.getQuantity() * product.getPrice());
                receipt.addPromotionDiscount(product.getPrice() * freeQuantity);
            }
        }
        return receipt;
    }
}
