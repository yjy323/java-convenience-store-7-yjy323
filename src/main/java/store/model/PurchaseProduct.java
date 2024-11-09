package store.model;

public class PurchaseProduct {
    private final String name;
    private final int quantity;

    public PurchaseProduct(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
