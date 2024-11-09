package store.view;

import static store.Messages.INVENTORY_STATUS;
import static store.Messages.PRODUCT_STATUS;
import static store.Messages.PROMOTION_PRODUCT_STATUS;
import static store.Messages.WELCOME;

import java.text.NumberFormat;
import java.util.List;
import store.dto.ProductStatus;

public class ProductOutputView {

    private String formatDecimal(int value) {
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(value);
    }

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

    public void print(List<ProductStatus> inventoryStatus) {
        System.out.println(WELCOME.getMessage());
        System.out.println(INVENTORY_STATUS.getMessage());
        System.out.println();

        for (ProductStatus productStatus : inventoryStatus) {
            System.out.println(productStatusLine(productStatus));
        }
        System.out.println();
    }
}
