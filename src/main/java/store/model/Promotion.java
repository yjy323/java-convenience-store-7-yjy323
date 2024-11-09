package store.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Promotion {

    public static final int BUY_MIN_VALUE = 1;
    public static final int FREE_MIN_VALUE = 1;
    
    private String name;
    private int buy;
    private int free;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Promotion(String name, int buy, int free, LocalDateTime startTime, LocalDateTime endTime) {
        validate(name, buy, free, startTime, endTime);
        this.name = name;
        this.buy = buy;
        this.free = free;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validate(String name, int buy, int free, LocalDateTime startTime, LocalDateTime endTime) {
        validateDateRange(startTime, endTime);
    }

    private void validateDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException();
        }
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
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
