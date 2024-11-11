package store.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.dto.PaymentDto;
import store.dto.ProductDto;
import store.model.Payment;
import store.model.Product;
import store.model.Promotion;
import store.model.Purchase;

class PaymentServiceTest {

    private PaymentService paymentService;

    private static final LocalDate START = LocalDate.of(2024, 1, 1);
    private static final LocalDate END = LocalDate.of(2024, 12, 31);
    private static final Promotion TWO_PLUS_ONE = new Promotion("2+1", 2, 1, START, END);
    private static final Promotion ONE_PLUS_ONE = new Promotion("1+1", 1, 1, START, END);

    private static final String PROM_PROD_21 = "2+1행사+일반";
    private static final String PROM_PROD_11 = "1+1행사+일반";
    private static final String PROD_ONLY = "일반";
    private static final String EXP = "2+1행사";
    private static final String LESS_EXP = "2+1행사";
    private static final int QUANTITY_ISN_T_IMPORTANT = 1000;

    private static final Product PRODUCT_ONLY = new Product(PROD_ONLY, 1000, QUANTITY_ISN_T_IMPORTANT, null);
    private static final Product PRODUCT_21 = new Product(PROM_PROD_21, 1000, QUANTITY_ISN_T_IMPORTANT, null);
    private static final Product PRODUCT_11 = new Product(PROM_PROD_11, 1000, QUANTITY_ISN_T_IMPORTANT, null);
    private static final Product PRODUCT_PROM_21 =
            new Product(PROM_PROD_21, 1000, QUANTITY_ISN_T_IMPORTANT, TWO_PLUS_ONE);
    private static final Product PRODUCT_PROM_11 =
            new Product(PROM_PROD_11, 1000, QUANTITY_ISN_T_IMPORTANT, ONE_PLUS_ONE);
    private static final Product MEMBERSHIP_MAX = new Product(EXP, 27000, QUANTITY_ISN_T_IMPORTANT, null);
    private static final Product MEMBERSHIP_NEAR_MAX = new Product(LESS_EXP, 26000, QUANTITY_ISN_T_IMPORTANT, null);
    private static final Product MEMBERSHIP_MAX_PROM = new Product(EXP, 27000, QUANTITY_ISN_T_IMPORTANT, ONE_PLUS_ONE);
    private static final Product MEMBERSHIP_NEAR_MAX_PROM =
            new Product(LESS_EXP, 26000, QUANTITY_ISN_T_IMPORTANT, ONE_PLUS_ONE);

    private Purchase create(Product product, int quantity) {
        return new Purchase(product, quantity);
    }

    @Test
    public void 일반상품_멤버십X_결제테스트() throws Exception {
        //Given
        List<Purchase> purchases = List.of(create(PRODUCT_ONLY, 1));
        Payment payment = new Payment(purchases);
        paymentService = new PaymentService(payment);

        //When
        PaymentDto dto = paymentService.paymentProcess();

        //Then
        assertThat(dto.getTotalQuantity()).isEqualTo(1);
        assertThat(dto.getRegularTotalPrice()).isEqualTo(1000);
        assertThat(dto.getMembershipDiscount()).isEqualTo(0);
    }

    @Test
    public void 일반상품_멤버십O_결제테스트() throws Exception {
        //Given
        List<Purchase> purchases = List.of(create(PRODUCT_ONLY, 1));
        Payment payment = new Payment(purchases);
        payment.applyMembership();
        paymentService = new PaymentService(payment);

        //When
        PaymentDto dto = paymentService.paymentProcess();

        //Then
        assertThat(dto.getTotalQuantity()).isEqualTo(1);
        assertThat(dto.getRegularTotalPrice()).isEqualTo(1000);
        assertThat(dto.getMembershipDiscount()).isEqualTo(300);
    }

    @Test
    public void 행사상품_멤버십X_결제테스트() throws Exception {
        //Given
        List<Purchase> purchases = List.of(create(PRODUCT_PROM_11, 2));
        Payment payment = new Payment(purchases);
        paymentService = new PaymentService(payment);

        //When
        PaymentDto dto = paymentService.paymentProcess();

        //Then
        assertThat(dto.getTotalQuantity()).isEqualTo(2);
        assertThat(dto.getPromotionTotalPrice()).isEqualTo(2000);
        assertThat(dto.getPromotionDiscount()).isEqualTo(1000);
        assertThat(dto.getMembershipDiscount()).isEqualTo(0);
    }

    @Test
    public void 행사상품_멤버십O_결제테스트() throws Exception {
        //Given
        List<Purchase> purchases = List.of(create(PRODUCT_PROM_11, 2));
        Payment payment = new Payment(purchases);
        payment.applyMembership();
        paymentService = new PaymentService(payment);

        //When
        PaymentDto dto = paymentService.paymentProcess();

        //Then
        assertThat(dto.getTotalQuantity()).isEqualTo(2);
        assertThat(dto.getPromotionTotalPrice()).isEqualTo(2000);
        assertThat(dto.getPromotionDiscount()).isEqualTo(1000);
        assertThat(dto.getMembershipDiscount()).isEqualTo(0);
    }

    @Test
    public void 행사_일반_재고동시사용_테스트() throws Exception {
        //Given
        List<Purchase> purchases = List.of(create(PRODUCT_21, 1),
                create(PRODUCT_PROM_21, 3),
                create(PRODUCT_PROM_11, 2));
        Payment payment = new Payment(purchases);
        payment.applyMembership();
        paymentService = new PaymentService(payment);

        //When
        PaymentDto dto = paymentService.paymentProcess();

        //Then
        // 구매 가격
        assertThat(dto.getRegularTotalPrice()).isEqualTo(1000);
        assertThat(dto.getPromotionTotalPrice()).isEqualTo(5000);
        // 할인 가격
        assertThat(dto.getPromotionDiscount()).isEqualTo(2000);
        assertThat(dto.getMembershipDiscount()).isEqualTo(300);
        // 구매 수량
        assertThat(dto.getTotalQuantity()).isEqualTo(6);
        assertThat(dto.getProducts().stream().mapToInt(ProductDto::getQuantity).sum()).isEqualTo(6);
        assertThat(dto.getFreeProducts().stream().mapToInt(ProductDto::getQuantity).sum()).isEqualTo(2);

    }

    private static Stream<Arguments> maxFactory() {
        return Stream.of(
                Arguments.of(new Purchase(MEMBERSHIP_MAX, 1), true),
                Arguments.of(new Purchase(MEMBERSHIP_NEAR_MAX, 1), false),
                Arguments.of(new Purchase(MEMBERSHIP_NEAR_MAX, 2), true)
        );
    }

    @ParameterizedTest
    @MethodSource("maxFactory")
    public void 멤버십_최대_할인(Purchase purchase, boolean expected) throws Exception {
        //Given
        List<Purchase> purchases = List.of(purchase);
        Payment payment = new Payment(purchases);
        payment.applyMembership();
        paymentService = new PaymentService(payment);

        //When
        PaymentDto dto = paymentService.paymentProcess();

        //Then
        assertThat(dto.getMembershipDiscount() == 8000).isEqualTo(expected);
    }

    @Test
    public void 행사상품_멤버십_최대_할인() throws Exception {
        //Given
        List<Purchase> purchases = List.of(create(MEMBERSHIP_MAX_PROM, 2), create(MEMBERSHIP_NEAR_MAX_PROM, 2));
        Payment payment = new Payment(purchases);
        payment.applyMembership();
        paymentService = new PaymentService(payment);

        //When
        PaymentDto dto = paymentService.paymentProcess();

        //Then
        assertThat(dto.getMembershipDiscount()).isEqualTo(0);
    }

    @Test
    public void 행사상품_단품구매_멤버십_할인() throws Exception {
        //Given
        List<Purchase> purchases = List.of(create(PRODUCT_PROM_11, 1),
                create(PRODUCT_PROM_21, 2),
                create(PRODUCT_PROM_11, 2),
                create(PRODUCT_PROM_21, 3));
        Payment payment = new Payment(purchases);
        payment.applyMembership();
        paymentService = new PaymentService(payment);

        //When
        PaymentDto dto = paymentService.paymentProcess();

        //Then
        // 구매 가격
        assertThat(dto.getRegularTotalPrice()).isEqualTo(3000);
        assertThat(dto.getPromotionTotalPrice()).isEqualTo(5000);
        // 할인 가격
        assertThat(dto.getPromotionDiscount()).isEqualTo(2000);
        assertThat(dto.getMembershipDiscount()).isEqualTo(900);
        // 구매 수량
        assertThat(dto.getTotalQuantity()).isEqualTo(8);
        assertThat(dto.getProducts().stream().mapToInt(ProductDto::getQuantity).sum()).isEqualTo(8);
        assertThat(dto.getFreeProducts().stream().mapToInt(ProductDto::getQuantity).sum()).isEqualTo(2);
    }
}