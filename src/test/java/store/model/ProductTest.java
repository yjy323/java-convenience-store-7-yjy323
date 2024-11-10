package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.ErrorMessages.PURCHASE_NOT_ENOUGH_QUANTITY;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProductTest {

    private static Stream<Arguments> productFactory() {
        return Stream.of(
                Arguments.of(20, 20, 0),
                Arguments.of(20, 10, 10),
                Arguments.of(20, 0, 20)
        );
    }

    @ParameterizedTest
    @MethodSource("productFactory")
    public void 구매수량만큼_재고수량_감소한다(int stock, int buy, int expected) throws Exception {
        //Given
        Product product = new Product("테스트상품", 1000, stock, null);

        //When
        product.buy(buy);

        //Then
        assertThat(product.getQuantity()).isEqualTo(expected);
    }

    @Test
    public void 재고_수량보다_구매수량이_크다면_예외처리한다() throws Exception {
        //Given
        int stock = 20;
        int buy = stock + 1;
        Product product = new Product("테스트상품", 1000, stock, null);

        //When, Then
        assertThatThrownBy(() -> product.validateUpdateQuantity(buy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PURCHASE_NOT_ENOUGH_QUANTITY.getMessage());

        assertThatThrownBy(() -> product.buy(buy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PURCHASE_NOT_ENOUGH_QUANTITY.getMessage());
    }
}