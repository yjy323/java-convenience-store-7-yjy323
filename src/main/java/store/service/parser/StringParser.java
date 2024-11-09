package store.service.parser;

import static store.ErrorMessages.INVALID_DATE;
import static store.ErrorMessages.INVALID_INTEGER;
import static store.ErrorMessages.PURCHASE_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import store.model.PurchaseProduct;

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

    private static void validateHyphen(String input) {
        if (input.split("-").length != 2) {
            throw new IllegalArgumentException(PURCHASE_FORMAT.getMessage());
        }
    }

    private static void validateSquareBrackets(String input) {
        if (!input.startsWith("[") || !input.endsWith("]")) {
            throw new IllegalArgumentException(PURCHASE_FORMAT.getMessage());
        }
    }

    public static PurchaseProduct parsePurchaseProduct(String input) {
        validateSquareBrackets(input);
        input = input.substring(1, input.length() - 1);

        validateHyphen(input);
        String[] split = input.split("-");
        
        return new PurchaseProduct(split[0], parseInteger(split[1], 1));
    }
}
