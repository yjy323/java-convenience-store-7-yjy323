package store.model;

import static store.ErrorMessages.PURCHASE_NOT_ENOUGH_QUANTITY;

import java.util.Objects;
import java.util.Optional;

public class Product extends CatalogItem {

    public static final int PRICE_MIN_VALUE = 1;
    public static final int QUANTITY_MIN_VALUE = 0;

    private int price;
    private int quantity;
    private Optional<Promotion> promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = Optional.ofNullable(promotion);
    }

    public void validateUpdateQuantity(int quantity) {
        if (this.quantity - quantity < 0) {
            throw new IllegalArgumentException(PURCHASE_NOT_ENOUGH_QUANTITY.getMessage());
        }
    }

    public void buy(int quantity) {
        validateUpdateQuantity(quantity);
        this.quantity -= quantity;
    }

    /*
     * Getters
     * */


    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Optional<Promotion> getPromotion() {
        return promotion;
    }

    /*
     * Overrides
     * */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product product)) {
            return false;
        }
        return Objects.equals(name, product.name) && Objects.equals(promotion, product.promotion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, promotion);
    }
}
