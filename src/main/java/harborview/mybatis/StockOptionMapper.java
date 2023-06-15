package harborview.mybatis;

import harborview.domain.stockmarket.StockOption;
import harborview.domain.stockmarket.StockOptionPurchase;
import harborview.domain.stockmarket.StockOptionSale;

public interface StockOptionMapper {
    StockOption findStockOption(String ticker);
    void insertPurchase(StockOptionPurchase purchase);
    void insertSale(StockOptionSale sale);
    void insertStockOption(StockOption option);
}
