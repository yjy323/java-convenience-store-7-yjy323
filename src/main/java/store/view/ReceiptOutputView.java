package store.view;

import java.text.NumberFormat;
import store.dto.ProductStatus;
import store.dto.Receipt;

public class ReceiptOutputView {

    private String formatDecimal(int value) {
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(value);
    }

    public void print(Receipt receipt) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");
        for (ProductStatus status : receipt.getProducts()) {
            System.out.printf("%s\t\t%s\t%s\n", status.getName(), formatDecimal(status.getQuantity()),
                    formatDecimal(status.getPrice() * status.getQuantity()));
        }
        if (!receipt.getFreeProducts().isEmpty()) {
            System.out.println("=============증\t정===============");
            for (ProductStatus status : receipt.getFreeProducts()) {
                System.out.printf("%s\t\t%s\n", status.getName(), formatDecimal(status.getQuantity()));
            }
        }
        System.out.println("====================================");
        int totalPrice = receipt.getRegularTotalPrice() + receipt.getPromotionTotalPrice();
        int promotionDiscount = receipt.getPromotionDiscount();
        int membershipDiscount = 0;
        if (receipt.isMembershipStatus()) {
            membershipDiscount = Math.min((receipt.getRegularTotalPrice() / 100 * 30), 8000);
        }
        int paymentPrice = totalPrice - promotionDiscount - membershipDiscount;
        System.out.printf("총구매액\t\t%s\t%s\n", formatDecimal(receipt.getTotalQuantity()), formatDecimal(totalPrice));
        System.out.printf("행사할인\t\t\t-%s\n", formatDecimal(promotionDiscount));
        System.out.printf("멤버십할인\t\t\t-%s\n", formatDecimal(membershipDiscount));
        System.out.printf("내실돈\t\t\t %s\n", formatDecimal(paymentPrice));
        System.out.println();
    }
}
