package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.ErrorMessages.DUPLICATE_ITEM;
import static store.ErrorMessages.INVALID_INTEGER;

import java.util.List;
import org.junit.jupiter.api.Test;
import store.model.Catalog;
import store.model.Promotion;
import store.service.parser.PromotionCsvParser;

class PromotionCatalogServiceTest {
    private final PromotionCsvParser parser = new PromotionCsvParser();
    private final CatalogService<Promotion> catalogService = new CatalogService(parser);

    @Test
    public void 프로모션_Catalog_정상테스트() throws Exception {
        //Given
        String header = "name,buy,get,start_date,end_date";
        String promotionName1 = "탄산2+1";
        String promotionName2 = "MD추천상품";
        String promotion1 = promotionName1 + ",2,1,2024-01-01,2024-12-31";
        String promotion2 = promotionName2 + ",1,1,2024-01-01,2024-12-31";

        //When
        Catalog<Promotion> catalog = catalogService.create(List.of(header, promotion1, promotion2));

        //Then
        assertThat(catalog.getItems().stream().map(Promotion::getName).toList())
                .containsExactly(promotionName1, promotionName2);
    }

    @Test
    public void 프로모션_명이_중복된다면_예외처리한다() throws Exception {
        //Given
        String header = "name,buy,get,start_date,end_date";
        String promotionName1 = "탄산2+1";
        String promotionName2 = "탄산2+1";
        String promotion1 = promotionName1 + ",2,1,2024-01-01,2024-12-31";
        String promotion2 = promotionName2 + ",1,1,2024-01-01,2024-12-31";

        //When, Then
        assertThatThrownBy(() -> catalogService.create(List.of(header, promotion1, promotion2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DUPLICATE_ITEM.getMessage());
    }

    @Test
    public void 프로모션_증정개수가_1이아니라면_예외처리한다() throws Exception {
        //Given
        String header = "name,buy,get,start_date,end_date";
        String promotion = String.format("탄산2+1,2,%d,2024-01-01,2024-12-31", 2);

        //When, Then
        assertThatThrownBy(() -> catalogService.create(List.of(header, promotion)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_INTEGER.getMessage());
    }
}