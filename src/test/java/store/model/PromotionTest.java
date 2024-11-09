package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.ErrorMessages.INVALID_DATE_RANGE;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PromotionTest {

    @Test
    public void Promotion_생성_정상테스트() throws Exception {
        //Given
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        //When
        Promotion promotion = new Promotion("테스트", 2, 1, startDate, endDate);

        //Then
        assertThat(promotion.getStartTime()).isEqualTo(startDate);
        assertThat(promotion.getEndTime()).isEqualTo(endDate);

    }

    @Test
    public void 시작일자가_종료일자보다_크다면_예외처리한다() throws Exception {
        //Given
        LocalDate startDate = LocalDate.of(2024, 12, 31);
        LocalDate endDate = LocalDate.of(2024, 1, 1);

        //When, Then
        assertThatThrownBy(() -> new Promotion("테스트", 2, 1, startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_DATE_RANGE.getMessage());

    }
}