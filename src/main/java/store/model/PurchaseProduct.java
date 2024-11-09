package store.model;

public class PurchaseProduct {

    private final Product product;
    private final int quantity;

    public PurchaseProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
