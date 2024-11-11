package store.model;

import java.util.Collections;
import java.util.List;

public class Catalog<T extends CatalogItem> {
    private final List<T> items;
    private final List<String> keySet;

    public Catalog(List<T> items) {
        this.items = items;
        keySet = items.stream().map(T::getName).distinct().toList();
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<String> getKeySet() {
        return keySet;
    }

    public T find(String key) {
        for (T item : items) {
            if (item.getName().equals(key)) {
                return item;
            }
        }
        return null;
    }
}
