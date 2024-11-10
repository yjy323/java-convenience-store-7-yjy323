package store.service.parser;

import static store.ErrorMessages.INVALID_DATE;
import static store.ErrorMessages.INVALID_INTEGER;
import static store.ErrorMessages.PURCHASE_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import store.dto.PurchaseDto;

public class StringParser {

    private static final String DATE_FORMAT = "uuuu-MM-dd";
    private static final String PRODUCT_NAME_QUANTITY_DELIMITER = "-";
    private static final String PRODUCT_WRAPPER_LEFT = "[";
    private static final String PRODUCT_WRAPPER_RIGHT = "]";
    private static final String PRODUCT_DELIMITER = ",";


    private static final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern(DATE_FORMAT)
            .withResolverStyle(ResolverStyle.STRICT);

    /*
     * Integer
     * */
    public static String parseName(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException(INVALID_INTEGER.getMessage());
        }
        return input;
    }

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

    /*
     * Date
     * */

    public static LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(INVALID_DATE.getMessage());
        }
    }

    /*
     * Order
     * */

    private static void validateNameQuantityFormat(String input) {
        if (input.split(PRODUCT_NAME_QUANTITY_DELIMITER).length != 2) {
            throw new IllegalArgumentException(PURCHASE_FORMAT.getMessage());
        }
    }

    private static void validateWrapperFormat(String input) {
        if (!input.startsWith(PRODUCT_WRAPPER_LEFT) || !input.endsWith(PRODUCT_WRAPPER_RIGHT)) {
            throw new IllegalArgumentException(PURCHASE_FORMAT.getMessage());
        }
    }

    public static PurchaseDto parsePurchaseDto(String input) {
        validateWrapperFormat(input);
        input = input.substring(1, input.length() - 1);
        validateNameQuantityFormat(input);
        String[] split = input.split(PRODUCT_NAME_QUANTITY_DELIMITER);
        
        try {
            return new PurchaseDto(parseName(split[0]), parseInteger(split[1], 1));
        } catch (Exception e) {
            throw new IllegalArgumentException(PURCHASE_FORMAT.getMessage());
        }
    }

    public static List<PurchaseDto> parsePurchaseDtoList(String input) {
        List<String> dataList = List.of(input.split(PRODUCT_DELIMITER));

        List<PurchaseDto> PurchaseDtoList = new ArrayList<>();
        for (String data : dataList) {
            PurchaseDtoList.add(StringParser.parsePurchaseDto(data));
        }
        return PurchaseDtoList;
    }
}
