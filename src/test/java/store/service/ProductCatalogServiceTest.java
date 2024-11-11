package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.ErrorMessages.DUPLICATE_ITEM;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Catalog;
import store.model.Product;
import store.model.Promotion;
import store.service.parser.ProductCsvParser;

class ProductCatalogServiceTest {
    private ProductCsvParser parser;
    private CatalogService<Product> catalogService;

    private Promotion createPromotion(String name) {
        LocalDate dummyDate = DateTimes.now().toLocalDate();
        return new Promotion(name, 1, 1, dummyDate, dummyDate);
    }

    @BeforeEach
    void setUp() {
        Catalog<Promotion> promotionCatalog = new Catalog<>(
                List.of(createPromotion("프로모션1"), createPromotion("프로모션2")));
        parser = new ProductCsvParser(promotionCatalog);
        catalogService = new CatalogService<>(parser);
    }

    @Test
    public void 상품_Catalog_정상테스트() throws Exception {
        //Given
        String header = "name,buy,get,start_date,end_date";
        String productName1 = "상품1";
        String productName2 = "상품2";
        String promotionName1 = "프로모션1";
        String promotionName2 = "프로모션2";
        String product1 = productName1 + ",1000,10," + promotionName1;
        String product2 = productName2 + ",1000,10," + promotionName2;

        //When
        Catalog<Product> catalog = catalogService.create(List.of(header, product1, product2));

        //Then
        assertThat(catalog.getItems().stream().map(Product::getName).toList())
                .containsExactly(productName1, productName2);
    }

    @Test
    public void 상품명만_중복_정상테스트() throws Exception {
        //Given
        String header = "name,buy,get,start_date,end_date";
        String productName1 = "상품1";
        String promotionName1 = "프로모션1";
        String promotionName2 = "프로모션2";
        String product1 = productName1 + ",1000,10," + promotionName1;
        String product2 = productName1 + ",1000,10," + promotionName2;

        //When
        Catalog<Product> catalog = catalogService.create(List.of(header, product1, product2));

        //Then
        assertThat(catalog.getItems().stream().map(Product::getName).toList())
                .containsExactly(productName1, productName1);
    }

    @Test
    public void 프로모션명만_중복_정상테스트() throws Exception {
        //Given
        String header = "name,buy,get,start_date,end_date";
        String productName1 = "상품1";
        String productName2 = "상품2";
        String promotionName1 = "프로모션1";
        String product1 = productName1 + ",1000,10," + promotionName1;
        String product2 = productName2 + ",1000,10," + promotionName1;

        //When
        Catalog<Product> catalog = catalogService.create(List.of(header, product1, product2));

        //Then
        assertThat(catalog.getItems().stream().map(Product::getName).toList())
                .containsExactly(productName1, productName2);
    }

    @Test
    public void 상품명_프로모션명이_중복이라면_예외처리한다() throws Exception {
        //Given
        String header = "name,buy,get,start_date,end_date";
        String productName1 = "상품1";
        String promotionName1 = "프로모션1";
        String product1 = productName1 + ",1000,10," + promotionName1;
        String product2 = productName1 + ",1000,10," + promotionName1;

        //When, Then
        assertThatThrownBy(() -> catalogService.create(List.of(header, product1, product2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DUPLICATE_ITEM.getMessage());
    }
}