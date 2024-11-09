package store.domain;

import java.util.Objects;
import java.util.Optional;

public class Product {


    public static final int PRICE_MIN_VALUE = 1;
    public static final int QUANTITY_MIN_VALUE = 0;

    private String name;
    private int price;
    private int quantity;
    private Optional<Promotion> promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = Optional.ofNullable(promotion);
    }

    /*
     * Getters
     * */

    public String getName() {
        return name;
    }

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