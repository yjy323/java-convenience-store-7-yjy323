package store.service.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.ErrorMessages.INVALID_DATE;
import static store.ErrorMessages.INVALID_INTEGER;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class StringParserTest {

    @ParameterizedTest
    @ValueSource(strings = {"-2147483648", "2147483647", "0", "-1", "1"})
    public void 정수_정상(String input) throws Exception {
        //Given
        int expected = Integer.parseInt(input);

        //When
        int actual = StringParser.parseInteger(input);

        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-2147483649", "2147483648"})
    public void int범위를_벗어난다면_예외처리한다(String input) throws Exception {
        //Given

        //When, Then
        assertThatThrownBy(() -> StringParser.parseInteger(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_INTEGER.getMessage());
    }

    private static Stream<Arguments> int_min_max_factory() {
        return Stream.of(
                Arguments.of("0", 1, Integer.MAX_VALUE),
                Arguments.of("1", Integer.MIN_VALUE, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("int_min_max_factory")
    public void min_max범위를_벗어난다면_예외처리한다(String input, int min, int max) throws Exception {
        //Given

        //When, Then
        assertThatThrownBy(() -> StringParser.parseInteger(input, min, max))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_INTEGER.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-01-01", "2024-12-31", "2024-02-29"})
    public void 날짜형식_정상(String input) throws Exception {
        //Given
        LocalDate expected = LocalDate.parse(input);

        //When
        LocalDate actual = StringParser.parseDate(input);

        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1900-02-29", "2023-02-29"})
    public void 윤년_유효하지않다면_예외처리한다(String input) throws Exception {
        //Given

        //When, Then
        assertThatThrownBy(() -> StringParser.parseDate(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_DATE.getMessage());

    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-01-32", "2024-04-31"})
    public void 날짜형식이_유효하지않다면_예외처리한다(String input) throws Exception {
        //Given

        //When, Then
        assertThatThrownBy(() -> StringParser.parseDate(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_DATE.getMessage());

    }
}