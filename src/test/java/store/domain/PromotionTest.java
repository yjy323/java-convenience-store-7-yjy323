package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PromotionTest {

    static final String name = "탄산2+1";
    static final String buy = "2";
    static final String free = "1";
    static final String start_date = "2024-01-01";
    static final String end_date = "2024-12-31";

    private static LocalDateTime createStartDate(int year, int month, int day) {
        return LocalDate.of(year, month, day).atStartOfDay();
    }

    private static LocalDateTime createEndDate(int year, int month, int day) {
        return LocalDate.of(year, month, day).atTime(LocalTime.MAX);
    }

    private static String createInput(String name, String buy, String free, String start_date, String end_date) {
        return String.format("%s,%s,%s,%s,%s", name, buy, free, start_date, end_date);
    }

    @Test
    public void 정상_테스트()
            throws Exception {
        //Given
        String input = createInput(name, buy, free, start_date, end_date);
        int expectedBuy = 2;
        int expectedFree = 1;
        LocalDateTime expectedStartDate = createStartDate(2024, 1, 1);
        LocalDateTime expectedEndDate = createEndDate(2024, 12, 31);

        //When
        Promotion actual = Promotion.createPromotion(input);

        //Then
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getBuy()).isEqualTo(expectedBuy);
        assertThat(actual.getFree()).isEqualTo(expectedFree);
        assertThat(actual.getStartTime()).isEqualTo(expectedStartDate);
        assertThat(actual.getEndTime()).isEqualTo(expectedEndDate);
    }

    private static Stream<Arguments> promotion_quantity_string_exception() {
        return Stream.of(
                Arguments.of(createInput(name, buy, "free", start_date, end_date)),
                Arguments.of(createInput(name, "buy", free, start_date, end_date))
        );
    }

    @ParameterizedTest
    @MethodSource("promotion_quantity_string_exception")
    public void 숫자X_예외테스트(String input)
            throws Exception {
        //Given

        //When, Then
        assertThatThrownBy(() -> Promotion.createPromotion(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> promotion_quantity_positive_exception() {
        return Stream.of(
                Arguments.of(createInput(name, buy, "0", start_date, end_date)),
                Arguments.of(createInput(name, buy, "-1", start_date, end_date)),
                Arguments.of(createInput(name, "0", free, start_date, end_date)),
                Arguments.of(createInput(name, "-1", free, start_date, end_date))
        );
    }

    @ParameterizedTest
    @MethodSource("promotion_quantity_positive_exception")
    public void 양수X_예외테스트(String input)
            throws Exception {
        //Given

        //When, Then
        assertThatThrownBy(() -> Promotion.createPromotion(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> promotion_quantity_int_exception() {
        return Stream.of(
                Arguments.of(createInput(name, buy, "2147483648", start_date, end_date)),
                Arguments.of(createInput(name, "2147483648", free, start_date, end_date))
        );
    }

    @ParameterizedTest
    @MethodSource("promotion_quantity_int_exception")
    public void int오버플로우_예외테스트(String input)
            throws Exception {
        //Given

        //When, Then
        assertThatThrownBy(() -> Promotion.createPromotion(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> promotion_date_format_exception() {
        return Stream.of(
                Arguments.of(createInput(name, buy, free, "2024-02-30", end_date)),
                Arguments.of(createInput(name, buy, free, start_date, "2024-02-30"))
        );
    }

    @ParameterizedTest
    @MethodSource("promotion_date_format_exception")
    public void 날짜형식_예외테스트(String input)
            throws Exception {
        //Given

        System.out.println(input);
        //When, Then
        assertThatThrownBy(() -> Promotion.createPromotion(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> promotion_date_range_exception() {
        return Stream.of(
                Arguments.of(createInput(name, buy, free, "2025-01-01", end_date)),
                Arguments.of(createInput(name, buy, free, start_date, "2023-12-31"))
        );
    }

    @ParameterizedTest
    @MethodSource("promotion_date_range_exception")
    public void 행사기간_예외테스트(String input)
            throws Exception {
        //Given
        //When, Then
        assertThatThrownBy(() -> Promotion.createPromotion(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}