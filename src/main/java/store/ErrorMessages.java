package store;

public enum ErrorMessages {

    DUPLICATE_ITEM("중복된 상품 또는 프로모션이 존재합니다."),
    NON_EXIST_PROMOTION("존재하지 않는 프로모션입니다."),

    FILE_OPEN_FAIL("%s 파일을 여는데 실패하였습니다."),
    FILE_READ_FAIL("%s 파일을 읽는데 실패하였습니다."),

    INVALID_CSV_FORMAT("유효하지 않은 CSV 파일 형식입니다."),
    INVALID_INTEGER("유효하지 않은 정수 형식입니다."),
    INVALID_DATE("유효하지 않은 날짜 형식입니다."),
    INVALID_DATE_RANGE("유효하지 않은 기간입니다.");

    private final String message;

    ErrorMessages(String message) {
        this.message = "[ERROR] " + message;
    }

    public String getMessage() {
        return message;
    }
}
