package harborview.domain.nordnet;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import harborview.domain.stockmarket.StockOptionTicker;

public record RiscResponse(StockOptionTicker ticker,
                           @JsonGetter("stockprice") double stockprice,
                           RiscResponseStatus status) {

    @Override
    @JsonIgnore
    public StockOptionTicker ticker() {
        return ticker;
    }

    @Override
    @JsonIgnore
    public RiscResponseStatus status() {
        return status;
    }

    @JsonGetter("ticker")
    public String getTickerJson() {
        return ticker.ticker();
    }

    @JsonGetter("status")
    public int statusJson() {
        return status.getStatus();
    }
}
