package store.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {
    private final List<ProductStatus> products = new ArrayList<>();
    private final List<ProductStatus> freeProducts = new ArrayList<>();
    private int regularTotalPrice = 0;
    private int promotionTotalPrice = 0;
    private int totalQuantity = 0;
    private int promotionDiscount = 0;
    private boolean membershipStatus = false;

    public List<ProductStatus> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public List<ProductStatus> getFreeProducts() {
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

    public void addProducts(ProductStatus product) {
        for (ProductStatus p : products) {
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

    public void addFreeProducts(ProductStatus product) {
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

    public boolean isMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(boolean membershipStatus) {
        this.membershipStatus = membershipStatus;
    }
}
