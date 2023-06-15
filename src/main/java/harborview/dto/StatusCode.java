package harborview.dto;

public enum StatusCode {
    RETRY(1),
    OK(2),
    ERROR(3),
    REQUEST_VALIDATION_ERROR(4),
    INSERT_DB_ERROR(5),
    INSERT_STOCK_OPTION_ERROR(6),
    PURCHASE_STOCK_OPTION_ERROR(7),
    SELL_STOCK_OPTION_ERROR(8);

    private final int status;

    StatusCode (int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
