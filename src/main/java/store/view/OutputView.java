package store.view;

import static store.Messages.INVENTORY_STATUS;
import static store.Messages.PRODUCT_STATUS;
import static store.Messages.PROMOTION_PRODUCT_STATUS;
import static store.Messages.RECEIPT_GIFT_FORMAT;
import static store.Messages.RECEIPT_GIFT_HEADER;
import static store.Messages.RECEIPT_HEADER;
import static store.Messages.RECEIPT_PRICE_FORMAT;
import static store.Messages.RECEIPT_PRODUCT_FORMAT;
import static store.Messages.RECEIPT_VERTICAL;
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

    public void printProducts(List<ProductDto> currentInventoryStatus) {
        System.out.println(WELCOME.getMessage());
        System.out.println(INVENTORY_STATUS.getMessage());
        System.out.println();

        for (ProductDto productDto : currentInventoryStatus) {
            System.out.println(productStatusLine(productDto));
        }
        System.out.println();
    }

    private void printReceiptProducts(PaymentDto paymentDto) {
        System.out.printf((RECEIPT_PRODUCT_FORMAT.getMessage()) + "%n", "상품명", "수량", "금액");
        for (ProductDto status : paymentDto.getProducts()) {
            int totalPrice = status.getPrice() * status.getQuantity();
            System.out.printf((RECEIPT_PRODUCT_FORMAT.getMessage()) + "%n",
                    status.getName(),
                    formatDecimal(status.getQuantity()), formatDecimal(totalPrice));
        }
    }

    private void printReceiptGifts(PaymentDto paymentDto) {
        if (!paymentDto.getFreeProducts().isEmpty()) {
            System.out.println(RECEIPT_GIFT_HEADER.getMessage());
            for (ProductDto status : paymentDto.getFreeProducts()) {
                System.out.printf((RECEIPT_GIFT_FORMAT.getMessage()) + "%n",
                        status.getName(),
                        formatDecimal(status.getQuantity()));
            }
        }
    }

    private void printReceiptDiscountPrices(PaymentDto paymentDto, int totalPrice) {
        int promotionDiscount = paymentDto.getPromotionDiscount();
        int membershipDiscount = paymentDto.getMembershipDiscount();
        int paymentPrice = totalPrice - promotionDiscount - membershipDiscount;

        System.out.printf((RECEIPT_PRICE_FORMAT.getMessage()) + "%n",
                "행사할인", "-" + formatDecimal(promotionDiscount));
        System.out.printf((RECEIPT_PRICE_FORMAT.getMessage()) + "%n",
                "멤버십할인", "-" + formatDecimal(membershipDiscount));
        System.out.printf((RECEIPT_PRICE_FORMAT.getMessage()) + "%n",
                "내실돈", formatDecimal(paymentPrice));
    }

    private void printReceiptPrices(PaymentDto paymentDto) {
        int totalPrice = paymentDto.getRegularTotalPrice() + paymentDto.getPromotionTotalPrice();
        int totalQuantity = paymentDto.getTotalQuantity();

        System.out.println(RECEIPT_VERTICAL.getMessage());

        System.out.printf((RECEIPT_PRODUCT_FORMAT.getMessage()) + "%n",
                "총구매액",
                formatDecimal(totalQuantity), formatDecimal(totalPrice));

        printReceiptDiscountPrices(paymentDto, totalPrice);
    }

    public void printReceipt(PaymentDto paymentDto) {
        System.out.println(RECEIPT_HEADER.getMessage());
        printReceiptProducts(paymentDto);
        printReceiptGifts(paymentDto);
        printReceiptPrices(paymentDto);
        System.out.println();
    }
}
