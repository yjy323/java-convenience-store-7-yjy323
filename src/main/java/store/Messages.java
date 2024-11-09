package store;

public enum Messages {
    WELCOME("안녕하세요. W편의점입니다."),
    PRODUCT_STATUS("- %s %s %s"),
    PROMOTION_PRODUCT_STATUS("- %s %s %s %s"),
    INVENTORY_STATUS("현재 보유하고 있는 상품입니다."),
    PURCHASE_GUIDE("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    CONFIRM_ADD_FREE("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"),
    CONFIRM_WITHOUT_PROMOTION("현재 %s %s개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
