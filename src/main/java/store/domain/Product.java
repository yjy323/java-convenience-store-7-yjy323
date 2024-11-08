package store.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Product {

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

    private void validate(String name, int price, int quantity, Promotion promotion) {
        validatePositiveInteger(price);
        validatePositiveInteger(quantity);
    }

    private void validatePositiveInteger(int value) {
        if (value < 1) {
            throw new IllegalArgumentException();
        }
    }

    public static Product createProduct(String input) {
        List<String> data = List.of(input.split(","));
        String name = data.get(0);
        int price = parseInteger(data.get(1));
        int quantity = parseInteger(data.get(2));
        Promotion promotion = null;
        return new Product(name, price, quantity, null);
    }

    private static int parseInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
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
        return this.promotion;
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
