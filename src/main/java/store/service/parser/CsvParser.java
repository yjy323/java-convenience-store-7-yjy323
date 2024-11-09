package store.service.parser;

import static store.ErrorMessages.INVALID_CSV_FORMAT;

public interface CsvParser<T> {

    String DELIMITER = ",";

    default void validateColumns(int MetaColumns, int ActualColumns) {
        if (MetaColumns != ActualColumns) {
            throw new IllegalArgumentException(INVALID_CSV_FORMAT.getMessage());
        }
    }

    T parse(String line);
}
