package store.view;

import static store.Messages.INVENTORY_STATUS;
import static store.Messages.PRODUCT_STATUS;
import static store.Messages.PROMOTION_PRODUCT_STATUS;
import static store.Messages.WELCOME;

import java.util.List;
import store.dto.ProductStatus;
import store.dto.Receipt;

public class OutputView implements View {

    private String formatQuantity(int quantity) {
        if (quantity == 0) {
            return "재고 없음";
        }
        return formatDecimal(quantity) + "개";
    }

    private String formatPrice(int price) {
        return formatDecimal(price) + "원";
    }

    private String productStatusLine(ProductStatus productStatus) {
        if (productStatus.getPromotionName().isEmpty()) {
            return String.format(PRODUCT_STATUS.getMessage(), productStatus.getName(),
                    formatPrice(productStatus.getPrice()),
                    formatQuantity(productStatus.getQuantity()));
        }
        return String.format(PROMOTION_PRODUCT_STATUS.getMessage(), productStatus.getName(),
                formatPrice(productStatus.getPrice()),
                formatQuantity(productStatus.getQuantity()),
                productStatus.getPromotionName());
    }

    public void printProducts(List<ProductStatus> inventoryStatus) {
        System.out.println(WELCOME.getMessage());
        System.out.println(INVENTORY_STATUS.getMessage());
        System.out.println();

        for (ProductStatus productStatus : inventoryStatus) {
            System.out.println(productStatusLine(productStatus));
        }
        System.out.println();
    }

    public void printReceipt(Receipt receipt) {
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
