package store.service;

import java.time.LocalDate;
import store.model.Promotion;

public class PromotionCsvParser implements CsvParser<Promotion> {


    public enum MetaData {
        NAME(0),
        BUY(1),
        FREE(2),
        START_DATE(3),
        END_DATE(4);

        private final int index;

        MetaData(final int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }


    public Promotion parse(String line) {
        String[] splitLine = line.split(DELIMITER);
        validateColumns(MetaData.values().length, splitLine.length);

        String name = splitLine[MetaData.NAME.getIndex()];
        int buy = StringParser.parseInteger(splitLine[MetaData.BUY.getIndex()], Promotion.BUY_MIN_VALUE);
        int free = StringParser.parseInteger(splitLine[MetaData.FREE.getIndex()], Promotion.FREE_MIN_VALUE);
        LocalDate startTime = StringParser.parseDate(splitLine[MetaData.START_DATE.getIndex()]);
        LocalDate endTime = StringParser.parseDate(splitLine[MetaData.END_DATE.getIndex()]);
        return new Promotion(name, buy, free, startTime, endTime);
    }
}
