package store;

public enum Messages {
    WELCOME("안녕하세요. W편의점입니다."),
    PRODUCT_STATUS("- %s %s %s"),
    PROMOTION_PRODUCT_STATUS("- %s %s %s %s"),
    INVENTORY_STATUS("현재 보유하고 있는 상품입니다."),
    PURCHASE_GUIDE("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
