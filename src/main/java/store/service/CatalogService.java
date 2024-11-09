package store.service;

import static store.ErrorMessages.DUPLICATE_ITEM;

import java.util.ArrayList;
import java.util.List;
import store.model.Catalog;

public class CatalogService<T> {

    private final CsvParser<T> csvParser;

    public CatalogService(CsvParser<T> csvParser) {
        this.csvParser = csvParser;
    }

    private void validateDuplicated(List<T> items, T item) {
        if (items.contains(item)) {
            throw new IllegalArgumentException(DUPLICATE_ITEM.getMessage());
        }
    }

    public Catalog<T> create(List<String> contents) {
        List<T> items = new ArrayList<>();

        for (int i = 1; i < contents.size(); i++) {
            T item = csvParser.parse(contents.get(i));
            validateDuplicated(items, item);
            items.add(item);
        }
        return new Catalog<>(items);
    }
}
