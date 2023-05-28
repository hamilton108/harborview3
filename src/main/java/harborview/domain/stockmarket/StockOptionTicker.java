package harborview.domain.stockmarket;

import java.util.Objects;

public record StockOptionTicker(String ticker) {
    public StockOptionTicker {
        Objects.requireNonNull(ticker);
    }
}
