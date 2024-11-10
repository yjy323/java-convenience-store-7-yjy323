package store.service;

public class PaymentService {

//    private final Payment payment;
//
//    public PaymentService(Payment payment) {
//        this.payment = payment;
//    }
//
//    public void confirmMembership(boolean confirm) {
//        if (confirm) {
//            payment.applyMembership();
//        }
//    }
//
//    private ProductDto createProductStatus(Product product) {
//        return new ProductDto(product.getName(), product.getPrice(), product.getQuantity());
//    }
//
//    public PaymentDto f() {
//        PaymentDto paymentDto = new PaymentDto();
//        for (PurchaseProduct purchaseProduct : purchaseTransaction.getItems()) {
//            Product product = purchaseProduct.getProduct();
//            paymentDto.addTotalQuantity(purchaseProduct.getQuantity());
//            if (product.getPromotion().isEmpty()) {
//                paymentDto.addProducts(
//                        new ProductDto(product.getName(), product.getPrice(), purchaseProduct.getQuantity()));
//                paymentDto.addRegularTotalPrice(purchaseProduct.getQuantity() * product.getPrice());
//            } else {
//                Promotion promotion = product.getPromotion().get();
//                int buyFree = promotion.getBuy() + promotion.getFree();
//                int freeQuantity = purchaseProduct.getQuantity() / buyFree;
//                paymentDto.addProducts(
//                        new ProductDto(product.getName(), product.getPrice(), purchaseProduct.getQuantity()));
//                paymentDto.addFreeProducts(new ProductDto(product.getName(), product.getPrice(), freeQuantity));
//                paymentDto.addPromotionTotalPrice(purchaseProduct.getQuantity() * product.getPrice());
//                paymentDto.addPromotionDiscount(product.getPrice() * freeQuantity);
//            }
//        }
//        if (payment.isMembershipStatus()) {
//            paymentDto.addMembershipDiscount(Math.min((paymentDto.getRegularTotalPrice() / 100 * 30), 8000));
//        }
//        return paymentDto;
//    }
//
//    public PaymentDto paymentProcess() {
//        PaymentDto paymentDto = new PaymentDto();
//        for (ProductDto productDto : payment.getProducts()) {
//
//        }
//        for (ProductDto productDto : payment.getFreeProducts()) {
//
//        }
//        return paymentDto;
//    }
}
