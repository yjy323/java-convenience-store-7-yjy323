package store.view;

import static store.Messages.INVENTORY_STATUS;
import static store.Messages.PRODUCT_STATUS;
import static store.Messages.PROMOTION_PRODUCT_STATUS;
import static store.Messages.WELCOME;

import java.util.List;
import store.dto.PaymentDto;
import store.dto.ProductDto;

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

    private String productStatusLine(ProductDto productDto) {
        if (productDto.getPromotionName().isEmpty()) {
            return String.format(PRODUCT_STATUS.getMessage(), productDto.getName(),
                    formatPrice(productDto.getPrice()),
                    formatQuantity(productDto.getQuantity()));
        }
        return String.format(PROMOTION_PRODUCT_STATUS.getMessage(), productDto.getName(),
                formatPrice(productDto.getPrice()),
                formatQuantity(productDto.getQuantity()),
                productDto.getPromotionName());
    }

    public void printProducts(List<ProductDto> inventoryStatus) {
        System.out.println(WELCOME.getMessage());
        System.out.println(INVENTORY_STATUS.getMessage());
        System.out.println();

        for (ProductDto productDto : inventoryStatus) {
            System.out.println(productStatusLine(productDto));
        }
        System.out.println();
    }

    public void printReceipt(PaymentDto paymentDto) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");
        for (ProductDto status : paymentDto.getProducts()) {
            System.out.printf("%s\t\t%s\t%s\n", status.getName(), formatDecimal(status.getQuantity()),
                    formatDecimal(status.getPrice() * status.getQuantity()));
        }
        if (!paymentDto.getFreeProducts().isEmpty()) {
            System.out.println("=============증\t정===============");
            for (ProductDto status : paymentDto.getFreeProducts()) {
                System.out.printf("%s\t\t%s\n", status.getName(), formatDecimal(status.getQuantity()));
            }
        }
        System.out.println("====================================");
        int totalPrice = paymentDto.getRegularTotalPrice() + paymentDto.getPromotionTotalPrice();
        int promotionDiscount = paymentDto.getPromotionDiscount();
        int membershipDiscount = paymentDto.getMembershipDiscount();
        int paymentPrice = totalPrice - promotionDiscount - membershipDiscount;
        System.out.printf("총구매액\t\t%s\t%s\n", formatDecimal(paymentDto.getTotalQuantity()), formatDecimal(totalPrice));
        System.out.printf("행사할인\t\t\t-%s\n", formatDecimal(promotionDiscount));
        System.out.printf("멤버십할인\t\t\t-%s\n", formatDecimal(membershipDiscount));
        System.out.printf("내실돈\t\t\t %s\n", formatDecimal(paymentPrice));
        System.out.println();
    }
}
