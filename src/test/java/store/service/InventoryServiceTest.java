package store.service;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import store.dto.ProductDto;

class ProductDisplayServiceTest extends ServiceTest {


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

    @Test
    void 파일에_있는_상품_목록_출력() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 5개",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
        });
    }
}