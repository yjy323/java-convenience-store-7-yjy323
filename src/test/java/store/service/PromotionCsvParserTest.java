package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static store.ErrorMessages.INVALID_CSV_FORMAT;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.Promotion;

class PromotionCsvParserTest {

    private final PromotionCsvParser parser = new PromotionCsvParser();

    @Test
    public void 프로모션_csv_파싱_정상테스트() throws Exception {
        //Given
        String expectedName = "탄산2+1";
        String content = "탄산2+1,2,1,2024-01-01,2024-12-31";

        //When
        Promotion promotion = parser.parse(content);

        //Then
        assertThat(promotion.getName()).isEqualTo(expectedName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"탄산2+1,2,1,2024-01-01", "탄산2+1,2,1,2024-01-01,2024-12-31,ERROR"})
    public void 컬럼이_유효하지않다면_예외처리한다(String content) throws Exception {
        //Given

        //When, Then
        Assertions.assertThatThrownBy(() -> parser.parse(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_CSV_FORMAT.getMessage());
    }
}