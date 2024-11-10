package store.model;

import java.util.Collections;
import java.util.List;
import store.dto.PaymentDto;
import store.dto.ProductDto;

public class Payment {
    private final List<Purchase> purchases;
    private boolean membershipStatus = false;

    public Payment(List<Purchase> purchases) {
        this.purchases = purchases;
    }


    public List<Purchase> getPurchases() {
        return Collections.unmodifiableList(purchases);
    }

    public boolean isMembershipStatus() {
        return membershipStatus;
    }

    public void applyMembership() {
        membershipStatus = true;
    }

    private void regularProductPayment(PaymentDto paymentDto, Product product, Purchase purchase) {
        if (product.getPromotion().isPresent()) {
            return;
        }
        paymentDto.addProducts(new ProductDto(product.getName(), product.getPrice(), purchase.getQuantity()));
        paymentDto.addRegularTotalPrice(purchase.getQuantity() * product.getPrice());
    }

    private void promotionGift(PaymentDto paymentDto, Product product, int freeQuantity) {
        if (freeQuantity > 0) {
            paymentDto.addFreeProducts(new ProductDto(product.getName(), product.getPrice(), freeQuantity));
            paymentDto.addPromotionDiscount(product.getPrice() * freeQuantity);
        }
    }

    private void promotionProductPayment(PaymentDto paymentDto, Product product, Purchase purchase) {
        if (product.getPromotion().isEmpty()) {
            return;
        }
        Promotion promotion = product.getPromotion().get();
        int freeQuantity = purchase.getQuantity() / (promotion.getBuy() + promotion.getFree());
        paymentDto.addProducts(new ProductDto(product.getName(), product.getPrice(), purchase.getQuantity()));
        paymentDto.addPromotionTotalPrice(purchase.getQuantity() * product.getPrice());
        promotionGift(paymentDto, product, freeQuantity);

    }

    private void updateProductStock(PaymentDto dto, Product product, int quantity) {
        product.buy(quantity);
        dto.addTotalQuantity(quantity);
    }

    private void membershipDiscountProcess(PaymentDto paymentDto) {
        if (membershipStatus) {
            paymentDto.addMembershipDiscount(Math.min((paymentDto.getRegularTotalPrice() / 100 * 30), 8000));
        }
    }

    public PaymentDto createPaymentDto() {
        PaymentDto paymentDto = new PaymentDto();

        for (Purchase purchase : purchases) {
            Product product = purchase.getProduct();
            updateProductStock(paymentDto, product, purchase.getQuantity());
            regularProductPayment(paymentDto, product, purchase);
            promotionProductPayment(paymentDto, product, purchase);
        }
        membershipDiscountProcess(paymentDto);
        return paymentDto;
    }
}
