package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.ErrorMessages.PURCHASE_NOT_ENOUGH_QUANTITY;

import camp.nextstep.edu.missionutils.Console;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Inventory;
import store.model.Payment;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;
import store.view.InputView;

class PurchaseServiceTest extends ServiceTest {

    private PurchaseService purchaseService;
    private static final int PRICE_ISN_T_IMPORTANT = 100;
    private static final LocalDate START = LocalDate.of(2024, 1, 1);
    private static final LocalDate END = LocalDate.of(2024, 12, 31);
    private static final Promotion TWO_PLUS_ONE = new Promotion("2+1", 2, 1, START, END);
    private static final Promotion ONE_PLUS_ONE = new Promotion("1+1", 1, 1, START, END);

    private static final String PROM_PROD_21 = "2+1행사+일반";
    private static final String PROM_PROD_11 = "1+1행사+일반";
    private static final String PROD_ONLY = "일반";
    private static final String PROM_21_ONLY = "2+1행사";
    private static final String PROM_11_ONLY = "1+1행사";


    @Override
    protected Inventory initProducts() {
        Inventory productInventory = new Inventory();
        productInventory.store(new Product(PROM_PROD_21, PRICE_ISN_T_IMPORTANT, 10, null));
        productInventory.store(new Product(PROM_PROD_11, PRICE_ISN_T_IMPORTANT, 10, null));
        productInventory.store(new Product(PROD_ONLY, PRICE_ISN_T_IMPORTANT, 10, null));
        productInventory.store(new Product(PROM_21_ONLY, PRICE_ISN_T_IMPORTANT, 0, null));
        productInventory.store(new Product(PROM_11_ONLY, PRICE_ISN_T_IMPORTANT, 0, null));
        return productInventory;
    }

    @Override
    protected Inventory initPromotionProducts() {
        Inventory promotionProductInventory = new Inventory();
        promotionProductInventory.store(new Product(PROM_PROD_21, PRICE_ISN_T_IMPORTANT, 10, TWO_PLUS_ONE));
        promotionProductInventory.store(new Product(PROM_PROD_11, PRICE_ISN_T_IMPORTANT, 10, ONE_PLUS_ONE));
        promotionProductInventory.store(new Product(PROM_21_ONLY, PRICE_ISN_T_IMPORTANT, 10, TWO_PLUS_ONE));
        promotionProductInventory.store(new Product(PROM_11_ONLY, PRICE_ISN_T_IMPORTANT, 10, ONE_PLUS_ONE));
        return promotionProductInventory;
    }

    @BeforeEach
    void setUp() {
        productInventory = initProducts();
        promotionProductInventory = initPromotionProducts();
        purchaseService = new PurchaseService(new InputView(), productInventory,
                promotionProductInventory);
    }

    @AfterEach
    void tearDown() {
        Console.close();
    }

    @Test
    public void 행사상품을_우선_구매한다() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\n", PROM_PROD_21, 10);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Product product = payment.getPurchases().getFirst().getProduct();

        //Then
        assertThat(product.getName()).isEqualTo(PROM_PROD_21);
        assertThat(product.getPromotion()).isPresent();
        assertThat(product.getPromotion().get()).isEqualTo(TWO_PLUS_ONE);
    }

    @Test
    public void 행사상품을_재고소진후_일반재고를_사용한다() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nY", PROM_PROD_21, 12);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase product = payment.getPurchases().getFirst();
        Purchase promotionProduct = payment.getPurchases().get(1);

        //Then
        assertThat(payment.getPurchases().size()).isEqualTo(2);

        assertThat(product.getProduct().getName()).isEqualTo(PROM_PROD_21);
        assertThat(product.getProduct().getPromotion()).isEmpty();
        assertThat(product.getQuantity()).isEqualTo(2);

        assertThat(promotionProduct.getProduct().getName()).isEqualTo(PROM_PROD_21);
        assertThat(promotionProduct.getProduct().getPromotion()).isPresent();
        assertThat(promotionProduct.getProduct().getPromotion().get()).isEqualTo(TWO_PLUS_ONE);
        assertThat(promotionProduct.getQuantity()).isEqualTo(10);
    }

    @Test
    public void 일반재고부족_행사재고부족_예외_테스트() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nN", PROM_11_ONLY, 12);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When, Then
        assertThatThrownBy(() -> purchaseService.purchaseProcess())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PURCHASE_NOT_ENOUGH_QUANTITY.getMessage());
    }
}