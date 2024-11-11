package store.service;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import store.model.Inventory;
import store.model.Product;

class PaymentServiceTest extends ServiceTest {

    @Override
    protected Inventory initProducts() {
        Inventory productInventory = new Inventory();
        productInventory.store(new Product("", 1000, 10, null));
        productInventory.store(new Product("", 1000, 10, null));
        return productInventory;
    }

    @Override
    protected Inventory initPromotionProducts() {
        Inventory promotionProductInventory = new Inventory();
        promotionProductInventory.store(new Product("", 1000, 10, null));
        promotionProductInventory.store(new Product("", 1000, 10, null));
        promotionProductInventory.store(new Product("", 1000, 10, null));
        promotionProductInventory.store(new Product("", 1000, 10, null));
        return promotionProductInventory;
    }

    private String eraseSpaces(String output) {
        return output.replaceAll("\\s", "");
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(eraseSpaces(output())).contains("내실돈18,300");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(eraseSpaces(output())).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }
}