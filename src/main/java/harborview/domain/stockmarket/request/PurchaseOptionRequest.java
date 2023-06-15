package harborview.domain.stockmarket.request;

public record PurchaseOptionRequest(String ticker,
                                    double ask,
                                    double bid,
                                    int volume,
                                    double spot,
                                    boolean rt)
        implements PurchaseOptionAble {

    public void validate() {
        PurchaseOptionValidation.validate(this);
    }

}
