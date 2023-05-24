package harborview.adapter;

import harborview.domain.stockmarket.Stock;
import harborview.domain.stockmarket.StockPrice;

import java.time.LocalDate;
import java.util.List;

public interface StockMarketAdapter {
    List<Stock> getStocks();
    List<StockPrice> getStockPrices(int tickerId, LocalDate fromDx);
    String oidToTicker(int oid);
}
