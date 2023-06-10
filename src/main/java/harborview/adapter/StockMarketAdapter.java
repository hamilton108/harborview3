package harborview.adapter;

import harborview.domain.stockmarket.Stock;
import harborview.domain.stockmarket.StockOptionPurchase;
import harborview.domain.stockmarket.StockPrice;
import harborview.domain.stockmarket.StockTicker;

import java.time.LocalDate;
import java.util.List;

public interface StockMarketAdapter {
    List<Stock> getStocks();
    List<StockPrice> getStockPrices(StockTicker ticker, LocalDate fromDx);
    List<StockOptionPurchase> activePurchasesWithCritters(int purchaseType);
    void toggleRule(int ruleId, boolean active, boolean isAccRule);
}
