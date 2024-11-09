package store.model;

import java.util.HashMap;

public class Inventory {
    private final HashMap<String, Product> stock = new HashMap<>();

    public void store(Product product) {
        stock.put(product.getName(), product);
    }
}
