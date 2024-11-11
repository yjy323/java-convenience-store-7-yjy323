package store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.ErrorMessages.PURCHASE_FORMAT;
import static store.ErrorMessages.PURCHASE_NON_EXIST;
import static store.ErrorMessages.PURCHASE_NOT_ENOUGH_QUANTITY;

import camp.nextstep.edu.missionutils.Console;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

    @ParameterizedTest
    @ValueSource(strings = {"[]", "[-1]", "[일반-]", "[일반-1],[]"})
    public void 입력형식_예외테스트(String simulatedInput) throws Exception {
        //Given
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When, Then
        assertThatThrownBy(() -> purchaseService.purchaseProcess())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PURCHASE_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"[없음-1]", "[일반-1],[없음-1]"})
    public void 없는상품_예외테스트(String simulatedInput) throws Exception {
        //Given
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When, Then
        assertThatThrownBy(() -> purchaseService.purchaseProcess())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PURCHASE_NON_EXIST.getMessage());
    }

    @Test
    public void 일반상품_구매_테스트() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\n", PROD_ONLY, 10);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Product product = payment.getPurchases().getFirst().getProduct();

        //Then
        assertThat(product.getName()).isEqualTo(PROD_ONLY);
        assertThat(product.getPromotion()).isEmpty();
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
    public void 행사상품_투플러스원_증정안내_재고수정O() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nY", PROM_PROD_21, 2);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase purchase = payment.getPurchases().getFirst();
        Product product = purchase.getProduct();

        //Then
        assertThat(product.getName()).isEqualTo(PROM_PROD_21);
        assertThat(product.getPromotion()).isPresent();
        assertThat(product.getPromotion().get()).isEqualTo(TWO_PLUS_ONE);
        assertThat(purchase.getQuantity()).isEqualTo(3);
    }

    @Test
    public void 행사상품_투플러스원_증정안내_재고수정X() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nN", PROM_PROD_21, 2);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase purchase = payment.getPurchases().getFirst();
        Product product = purchase.getProduct();

        //Then
        assertThat(product.getName()).isEqualTo(PROM_PROD_21);
        assertThat(product.getPromotion()).isPresent();
        assertThat(product.getPromotion().get()).isEqualTo(TWO_PLUS_ONE);
        assertThat(purchase.getQuantity()).isEqualTo(2);
    }

    @Test
    public void 행사상품_원플러스원_증정안내_재고수정O() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nY", PROM_PROD_11, 1);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase purchase = payment.getPurchases().getFirst();
        Product product = purchase.getProduct();

        //Then
        assertThat(product.getName()).isEqualTo(PROM_PROD_11);
        assertThat(product.getPromotion()).isPresent();
        assertThat(product.getPromotion().get()).isEqualTo(ONE_PLUS_ONE);
        assertThat(purchase.getQuantity()).isEqualTo(2);
    }

    @Test
    public void 행사상품_원플러스원_증정안내_재고수정X() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nN", PROM_PROD_11, 1);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase purchase = payment.getPurchases().getFirst();
        Product product = purchase.getProduct();

        //Then
        assertThat(product.getName()).isEqualTo(PROM_PROD_11);
        assertThat(product.getPromotion()).isPresent();
        assertThat(product.getPromotion().get()).isEqualTo(ONE_PLUS_ONE);
        assertThat(purchase.getQuantity()).isEqualTo(1);
    }

    @Test
    public void 행사상품_재고소진_일반재고로_수정O() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nY", PROM_PROD_21, 12);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase purchase = payment.getPurchases().getFirst();
        Purchase promotionPurchase = payment.getPurchases().get(1);

        //Then
        assertThat(payment.getPurchases().size()).isEqualTo(2);

        assertThat(purchase.getProduct().getName()).isEqualTo(PROM_PROD_21);
        assertThat(purchase.getProduct().getPromotion()).isEmpty();
        assertThat(purchase.getQuantity()).isEqualTo(2);

        assertThat(promotionPurchase.getProduct().getName()).isEqualTo(PROM_PROD_21);
        assertThat(promotionPurchase.getProduct().getPromotion()).isPresent();
        assertThat(promotionPurchase.getProduct().getPromotion().get()).isEqualTo(TWO_PLUS_ONE);
        assertThat(promotionPurchase.getQuantity()).isEqualTo(10);
    }

    @ParameterizedTest
    @ValueSource(ints = {12, 13})
    public void 행사상품_재고소진_일반재고로_수정X(int orderQuantity) throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nN", PROM_PROD_21, orderQuantity);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase promotionPurchase = payment.getPurchases().getFirst();

        //Then
        assertThat(payment.getPurchases().size()).isEqualTo(1);

        assertThat(promotionPurchase.getProduct().getName()).isEqualTo(PROM_PROD_21);
        assertThat(promotionPurchase.getProduct().getPromotion()).isPresent();
        assertThat(promotionPurchase.getProduct().getPromotion().get()).isEqualTo(TWO_PLUS_ONE);
        assertThat(promotionPurchase.getQuantity()).isEqualTo(9);
    }

    @ParameterizedTest
    @ValueSource(ints = {11})
    public void 행사상품_추가_증정_후_재고소진_일반재고로_수정X(int orderQuantity) throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nY\nN", PROM_PROD_21, orderQuantity);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase promotionPurchase = payment.getPurchases().getFirst();

        //Then
        assertThat(payment.getPurchases().size()).isEqualTo(1);

        assertThat(promotionPurchase.getProduct().getName()).isEqualTo(PROM_PROD_21);
        assertThat(promotionPurchase.getProduct().getPromotion()).isPresent();
        assertThat(promotionPurchase.getProduct().getPromotion().get()).isEqualTo(TWO_PLUS_ONE);
        assertThat(promotionPurchase.getQuantity()).isEqualTo(9);
    }

    @ParameterizedTest
    @ValueSource(ints = {11})
    public void 행사상품_추가_증정_후_재고소진_일반재고로_수정O(int orderQuantity) throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\nY\nY", PROM_PROD_21, orderQuantity);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When
        Payment payment = purchaseService.purchaseProcess();
        Purchase purchase = payment.getPurchases().getFirst();
        Purchase promotionPurchase = payment.getPurchases().get(1);

        //Then
        assertThat(payment.getPurchases().size()).isEqualTo(2);

        assertThat(purchase.getProduct().getName()).isEqualTo(PROM_PROD_21);
        assertThat(purchase.getProduct().getPromotion()).isEmpty();
        assertThat(purchase.getQuantity()).isEqualTo(2);

        assertThat(promotionPurchase.getProduct().getName()).isEqualTo(PROM_PROD_21);
        assertThat(promotionPurchase.getProduct().getPromotion()).isPresent();
        assertThat(promotionPurchase.getProduct().getPromotion().get()).isEqualTo(TWO_PLUS_ONE);
        assertThat(promotionPurchase.getQuantity()).isEqualTo(10);
    }

    @Test
    public void 일반재고_부족_예외_테스트() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\n", PROD_ONLY, 12);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When, Then
        assertThatThrownBy(() -> purchaseService.purchaseProcess())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PURCHASE_NOT_ENOUGH_QUANTITY.getMessage());
    }

    @Test
    public void 일반재고_및_행사재고_모두부족_예외_테스트() throws Exception {
        //Given
        String simulatedInput = String.format("[%s-%d]\n", PROM_11_ONLY, 12);
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        //When, Then
        assertThatThrownBy(() -> purchaseService.purchaseProcess())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PURCHASE_NOT_ENOUGH_QUANTITY.getMessage());
    }
}