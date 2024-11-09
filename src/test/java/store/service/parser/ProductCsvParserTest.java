package store.service.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.ErrorMessages.INVALID_CSV_FORMAT;
import static store.ErrorMessages.NON_EXIST_PROMOTION;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.Catalog;
import store.model.Product;
import store.model.Promotion;

class ProductCsvParserTest {

    private ProductCsvParser parser;

    private Promotion createPromotion(String name) {
        LocalDate dummyDate = LocalDate.now();
        return new Promotion(name, 1, 1, dummyDate, dummyDate);
    }

    @BeforeEach
    void setUp() {
        Catalog<Promotion> promotionCatalog = new Catalog<>(
                List.of(createPromotion("프로모션1"), createPromotion("프로모션2")));
        parser = new ProductCsvParser(promotionCatalog);
    }

    @Test
    public void 상품_csv_파싱_정상테스트() throws Exception {
        //Given
        String productName = "콜라";
        String promotionName = "프로모션1";
        String content = productName + ",1000,10," + promotionName;

        //When
        Product product = parser.parse(content);

        //Then
        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getPromotion()).isPresent();
        assertThat(product.getPromotion().get().getName()).isEqualTo(promotionName);
    }

    @Test
    public void 프로모션이_존재하지않는다면_예외처리한다() throws Exception {
        //Given
        String productName = "콜라";
        String promotionName = "프로모션3";
        String content = productName + ",1000,10," + promotionName;

        //When, Then
        assertThatThrownBy(() -> parser.parse(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NON_EXIST_PROMOTION.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"콜라,1000,10", "콜라,1000,10,프로모션1,ERROR"})
    public void 컬럼이_유효하지않다면_예외처리한다(String content) throws Exception {
        //Given

        //When, Then
        Assertions.assertThatThrownBy(() -> parser.parse(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_CSV_FORMAT.getMessage());
    }
}