package harborview.adapter;

import harborview.domain.stockmarket.Stock;
import harborview.domain.stockmarket.StockPrice;

import java.time.LocalDate;
import java.util.List;

public interface StockMarketAdapter {
    List<Stock> getStocks();
    List<StockPrice> getStockPrices(int tickerId, LocalDate fromDx);


}
/*
(def critter-purchases
            (pu/default-json-response ::critterpurchases 200
                    (fn [_ req]
                    (let [ptypes (get-in req [:path-params :ptype])
    ptype (cu/rs ptypes)
    items (.activePurchasesWithCritters @db-adapter ptype)]
            (map #(OptionPurchaseDTO. %) items)))))

 */
