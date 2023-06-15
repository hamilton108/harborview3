package harborview.domain.stockmarket.request;

public interface PurchaseOptionAble {
    String ticker();
    double ask();
    double bid();
    int volume();
    double spot();
    boolean rt();
}
