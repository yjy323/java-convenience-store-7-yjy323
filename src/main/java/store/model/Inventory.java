package store.model;

import java.util.HashMap;
import java.util.Optional;

public class Inventory {
    private final HashMap<String, Product> stock = new HashMap<>();

    public void store(Product product) {
        stock.put(product.getName(), product);
    }

    public Optional<Product> search(String key) {
        return Optional.ofNullable(stock.get(key));
    }
}
