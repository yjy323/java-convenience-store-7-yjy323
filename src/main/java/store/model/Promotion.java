package store.model;

import static store.ErrorMessages.INVALID_DATE_RANGE;

import java.time.LocalDate;
import java.util.Objects;

public class Promotion {

    public static final int BUY_MIN_VALUE = 1;
    public static final int FREE_MIN_VALUE = 1;
    public static final String NON_PROMOTION = "null";

    private String name;
    private int buy;
    private int free;
    private LocalDate startTime;
    private LocalDate endTime;

    public Promotion(String name, int buy, int free, LocalDate startTime, LocalDate endTime) {
        validate(name, buy, free, startTime, endTime);
        this.name = name;
        this.buy = buy;
        this.free = free;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validate(String name, int buy, int free, LocalDate startTime, LocalDate endTime) {
        validateDateRange(startTime, endTime);
    }

    private void validateDateRange(LocalDate startTime, LocalDate endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException(INVALID_DATE_RANGE.getMessage());
        }
    }

    public boolean isPromotionPeriod(LocalDate now) {
        return !now.isBefore(startTime) && !now.isAfter(endTime);
    }

    /*
     * Getters
     * */
    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getFree() {
        return free;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }


    /*
     * Overrides
     * */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Promotion promotion)) {
            return false;
        }
        return Objects.equals(name, promotion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
