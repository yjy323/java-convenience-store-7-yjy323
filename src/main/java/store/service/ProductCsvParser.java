package store.service;

import static store.ErrorMessages.NON_EXIST_PROMOTION;
import static store.model.Product.PRICE_MIN_VALUE;
import static store.model.Product.QUANTITY_MIN_VALUE;
import static store.model.Promotion.NON_PROMOTION;

import store.model.Catalog;
import store.model.Product;
import store.model.Promotion;

public class ProductCsvParser implements CsvParser<Product> {

    public enum MetaData {
        NAME(0),
        PRICE(1),
        QUANTITY(2),
        PROMOTION(3);

        private final int index;

        MetaData(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private final Catalog<Promotion> promotionCatalog;

    public ProductCsvParser(Catalog<Promotion> promotionCatalog) {
        this.promotionCatalog = promotionCatalog;
    }

    private Promotion searchPromotion(String promotionName) {
        if (promotionName.equals(NON_PROMOTION)) {
            return null;
        }
        for (Promotion promotion : promotionCatalog.getItems()) {
            if (promotion.getName().equals(promotionName)) {
                return promotion;
            }
        }
        throw new IllegalArgumentException(NON_EXIST_PROMOTION.getMessage());
    }

    public Product parse(String line) {
        String[] splitLine = line.split(DELIMITER);
        validateColumns(MetaData.values().length, splitLine.length);

        String name = splitLine[MetaData.NAME.getIndex()];
        int price = StringParser.parseInteger(splitLine[MetaData.PRICE.getIndex()], PRICE_MIN_VALUE);
        int quantity = StringParser.parseInteger(splitLine[MetaData.QUANTITY.getIndex()], QUANTITY_MIN_VALUE);
        Promotion promotion = searchPromotion(splitLine[MetaData.PROMOTION.getIndex()]);
        return new Product(name, price, quantity, promotion);
    }
}
