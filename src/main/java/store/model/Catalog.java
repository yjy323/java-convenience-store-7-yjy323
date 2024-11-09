package store.model;

import java.util.Collections;
import java.util.List;

public class Catalog<T> {
    private final List<T> items;

    public Catalog(List<T> items) {
        this.items = items;
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }
}
