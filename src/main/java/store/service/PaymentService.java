package store.service;

import store.dto.PaymentDto;
import store.dto.ProductDto;
import store.model.Payment;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;

public class PaymentService {

    private final Payment payment;

    public PaymentService(Payment payment) {
        this.payment = payment;
    }

    public void confirmMembership(boolean confirm) {
        if (confirm) {
            payment.applyMembership();
        }
    }

    private void regularProductPayment(PaymentDto paymentDto, Product product, Purchase purchase) {
        if (product.getPromotion().isPresent()) {
            return;
        }
        paymentDto.addProducts(new ProductDto(product.getName(), product.getPrice(), purchase.getQuantity()));
        paymentDto.addRegularTotalPrice(purchase.getQuantity() * product.getPrice());
    }

    private void promotionProductPayment(PaymentDto paymentDto, Product product, Purchase purchase) {
        if (product.getPromotion().isEmpty()) {
            return;
        }
        Promotion promotion = product.getPromotion().get();
        int buyFree = promotion.getBuy() + promotion.getFree();
        int freeQuantity = purchase.getQuantity() / buyFree;
        paymentDto.addProducts(new ProductDto(product.getName(), product.getPrice(), purchase.getQuantity()));
        paymentDto.addFreeProducts(new ProductDto(product.getName(), product.getPrice(), freeQuantity));
        paymentDto.addPromotionTotalPrice(purchase.getQuantity() * product.getPrice());
        paymentDto.addPromotionDiscount(product.getPrice() * freeQuantity);
    }

    public PaymentDto paymentProcess() {
        PaymentDto paymentDto = new PaymentDto();

        for (Purchase purchase : payment.getPurchases()) {
            Product product = purchase.getProduct();
            paymentDto.addTotalQuantity(product.getQuantity());
            regularProductPayment(paymentDto, product, purchase);
            promotionProductPayment(paymentDto, product, purchase);
        }
        if (payment.isMembershipStatus()) {
            paymentDto.addMembershipDiscount(Math.min((paymentDto.getRegularTotalPrice() / 100 * 30), 8000));
        }
        return paymentDto;
    }
}
