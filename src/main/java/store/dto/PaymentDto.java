package store.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentDto {
    private final List<ProductDto> products = new ArrayList<>();
    private final List<ProductDto> freeProducts = new ArrayList<>();
    private int regularTotalPrice = 0;
    private int promotionTotalPrice = 0;
    private int totalQuantity = 0;
    private int promotionDiscount = 0;
    private int membershipDiscount = 0;

    public List<ProductDto> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<ProductDto> getFreeProducts() {
        return Collections.unmodifiableList(freeProducts);
    }

    public int getRegularTotalPrice() {
        return regularTotalPrice;
    }

    public int getPromotionTotalPrice() {
        return promotionTotalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public void addProducts(ProductDto product) {
        for (ProductDto p : products) {
            if (p.getName().equals(product.getName())) {
                p.setQuantity(p.getQuantity() + product.getQuantity());
                return;
            }
        }
        products.add(product);
    }

    public void addRegularTotalPrice(int regularPrice) {
        this.regularTotalPrice += regularPrice;
    }

    public void addFreeProducts(ProductDto product) {
        freeProducts.add(product);
    }

    public void addPromotionTotalPrice(int promotionPrice) {
        this.promotionTotalPrice += promotionPrice;
    }

    public void addTotalQuantity(int quantity) {
        this.totalQuantity += quantity;
    }

    public void addPromotionDiscount(int promotionDiscount) {
        this.promotionDiscount += promotionDiscount;
    }

    public void addMembershipDiscount(int membershipDiscount) {
        this.membershipDiscount += membershipDiscount;
    }
}
