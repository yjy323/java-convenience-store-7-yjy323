package store.dto;

import java.util.Objects;

public class ProductDto {

    private String name;
    private int price;
    private int quantity;
    private String promotionName;

    public ProductDto(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = "";
    }

    public ProductDto(String name, int price, int quantity, String promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDto that)) {
            return false;
        }
        return Objects.equals(name, that.name) && Objects.equals(promotionName, that.promotionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, promotionName);
    }
}
