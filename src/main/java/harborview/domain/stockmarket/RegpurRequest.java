package harborview.domain.stockmarket;

public record RegpurRequest(String ticker, double ask, double bid, int volume, double spot, boolean rt, int stockId,
                            String opType, String expiry, double x) {
}

