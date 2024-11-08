package store.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProductTest {

    static final String name = "콜라";
    static final String price = "1000";
    static final String quantity = "10";
    static final String promotion = "null";

    private static String createInput(String name, String price, String quantity, String promotion) {
        return String.format("%s,%s,%s,%s", name, price, quantity, promotion);
    }

    @Test
    public void 정상_테스트()
            throws Exception {
        //Given
        String input = createInput(name, price, quantity, promotion);

        //When
        Product actual = Product.createProduct(input);
        Optional<Promotion> actualPromotion = actual.getPromotion();

        //Then
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(1000);
        assertThat(actual.getQuantity()).isEqualTo(10);
        assertThat(actualPromotion).isEqualTo(Optional.empty());
    }

    private static Stream<Arguments> promotion_quantity_string_exception() {
        return Stream.of(
                Arguments.of(createInput(name, "price", quantity, promotion)),
                Arguments.of(createInput(name, price, "quantity", promotion))
        );
    }

    @ParameterizedTest
    @MethodSource("promotion_quantity_string_exception")
    public void 숫자X_예외테스트(String input)
            throws Exception {
        //Given

        //When, Then
        assertThatThrownBy(() -> Product.createProduct(input))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> promotion_quantity_positive_exception() {
        return Stream.of(
                Arguments.of(createInput(name, "0", quantity, promotion)),
                Arguments.of(createInput(name, price, "0", promotion)),
                Arguments.of(createInput(name, "-1", quantity, promotion)),
                Arguments.of(createInput(name, price, "-1", promotion))
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
                Arguments.of(createInput(name, "2147483648", quantity, promotion)),
                Arguments.of(createInput(name, price, "2147483648", promotion))
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
}