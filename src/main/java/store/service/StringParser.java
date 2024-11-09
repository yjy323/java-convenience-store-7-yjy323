package store.service;

import static store.ErrorMessages.INVALID_DATE;
import static store.ErrorMessages.INVALID_INTEGER;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class StringParser {

    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);

    public static int parseInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_INTEGER.getMessage());
        }
    }

    public static int parseInteger(String input, int min) {
        try {
            int value = Integer.parseInt(input);
            if (value < min) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_INTEGER.getMessage());
        }
    }

    public static int parseInteger(String input, int min, int max) {
        try {
            int value = Integer.parseInt(input);
            if (value < min || value > max) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_INTEGER.getMessage());
        }
    }

    public static LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(INVALID_DATE.getMessage());
        }
    }
}
