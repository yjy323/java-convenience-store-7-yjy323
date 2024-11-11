package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.dto.ProductDto;
import store.model.Inventory;
import store.model.Product;
import store.model.Promotion;

class InventoryServiceTest {

    private final Inventory productInventory = createProducts();
    private final Inventory promotionProductInventory = createPromotionProducts();

    private static final int SIZE = 4;
    private static final int FIX_PRICE = 1000;
    private static final int FIX_QUANTITY = 10;
    private static final String FIX_PRODUCT_NAME = "상품";
    private static final String FIX_PROMOTION_NAME = "프로모션";

    private List<String> keySets() {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            keys.add(FIX_PRODUCT_NAME + i);
        }
        return keys;
    }

    private Inventory createPromotionProducts() {
        Inventory promotionProductInventory = new Inventory();
        LocalDate now = DateTimes.now().toLocalDate();
        Promotion promotion = new Promotion(FIX_PROMOTION_NAME, 2, 1, now, now);
        for (int i = 0; i < SIZE; i++) {
            Product product = new Product(FIX_PRODUCT_NAME + i, FIX_PRICE, FIX_QUANTITY, promotion);
            promotionProductInventory.store(product);
        }
        return promotionProductInventory;
    }

    private Inventory createProducts() {
        Inventory productInventory = new Inventory();
        for (int i = 0; i < SIZE / 2; i++) {
            Product product = new Product(FIX_PRODUCT_NAME + i, FIX_PRICE, FIX_QUANTITY, null);
            productInventory.store(product);
        }
        return productInventory;
    }

    private List<ProductDto> createProductDtoList() {
        List<ProductDto> productList = new ArrayList<>();
        int quantity = FIX_QUANTITY;
        for (int i = 0; i < SIZE; i++) {
            if (i == SIZE / 2) {
                quantity = 0;
            }
            productList.add(createProductDto(i, FIX_QUANTITY, FIX_PROMOTION_NAME));
            productList.add(createProductDto(i, quantity, ""));
        }
        return productList;
    }

    private ProductDto createProductDto(int idx, int quantity, String promotionName) {
        return new ProductDto(FIX_PRODUCT_NAME + idx, FIX_PRICE, quantity, promotionName);
    }

    @Test
    public void 불러온_상품목록_테스트() throws Exception {
        //Given
        InventoryService inventoryService = new InventoryService(productInventory, promotionProductInventory);
        List<ProductDto> expected = createProductDtoList();
        //When
        List<ProductDto> actual = inventoryService.getCurrentInventoryStatus(keySets());

        //Then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}