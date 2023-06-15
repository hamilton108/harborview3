package harborview.domain.stockmarket.request;


public record RegpurRequest(String ticker, double ask, double bid, int volume, double spot, boolean rt,
                            int stockId, String opType, String expiry, double x) implements PurchaseOptionAble {
    public void validate() {
        PurchaseOptionValidation.validate(this);
        if (stockId < 0) {
            throw new RuntimeException("stockId must be greater than zero");
        }
        if (x < 0) {
            throw new RuntimeException("x must be greater than zero");
        }
    }
}

