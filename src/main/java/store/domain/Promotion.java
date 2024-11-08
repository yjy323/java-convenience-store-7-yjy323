package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Objects;

public class Promotion {

    private String name;
    private int buy;
    private int free;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Promotion(String name, int buy, int free, LocalDateTime startTime, LocalDateTime endTime) {
        validate(name, buy, free, startTime, endTime);
        this.name = name;
        this.buy = buy;
        this.free = free;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private void validate(String name, int buy, int free, LocalDateTime startTime, LocalDateTime endTime) {
        validatePositiveInteger(buy);
        validatePositiveInteger(free);
        validateDateRange(startTime, endTime);
    }

    private void validatePositiveInteger(int value) {
        if (value < 1) {
            throw new IllegalArgumentException();
        }
    }

    private void validateDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException();
        }
    }

    public static Promotion createPromotion(String input) {
        List<String> data = List.of(input.split(","));
        String name = data.get(0);
        int buy = parseInteger(data.get(1));
        int free = parseInteger(data.get(2));
        LocalDateTime startTime = parseDateTime(data.get(3), LocalTime.MIDNIGHT);
        LocalDateTime endTime = parseDateTime(data.get(4), LocalTime.MAX);
        return new Promotion(name, buy, free, startTime, endTime);
    }

    private static int parseInteger(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }

    private static LocalDateTime parseDateTime(String input, LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
        try {
            return LocalDate.parse(input, formatter).atTime(time);
        } catch (DateTimeParseException e) {
            System.out.println(e.getMessage());
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
