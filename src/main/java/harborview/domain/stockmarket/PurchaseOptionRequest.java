package harborview.domain.stockmarket;

public record PurchaseOptionRequest(String ticker, double ask, double bid, int volume, double spot, boolean rt) {
}
