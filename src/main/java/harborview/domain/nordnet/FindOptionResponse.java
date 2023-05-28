package harborview.domain.nordnet;

import com.fasterxml.jackson.annotation.JsonSetter;

public class FindOptionResponse {
    private StockPrice stockPrice;
    private StockOption stockOption;


    public StockPrice getStockPrice() {
        return stockPrice;
    }

    @JsonSetter("stock-price")
    public void setStockPrice(StockPrice stockPrice) {
        this.stockPrice = stockPrice;
    }

    public StockOption getStockOption() {
        return stockOption;
    }

    @JsonSetter("option")
    public void setStockOption(StockOption stockOption) {
        this.stockOption = stockOption;
    }
}
