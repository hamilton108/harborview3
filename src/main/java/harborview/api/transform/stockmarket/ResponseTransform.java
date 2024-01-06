package harborview.api.transform.stockmarket;

import harborview.api.response.stockmarket.StockOptionPurchaseResponse;
import harborview.domain.stockmarket.StockOptionPurchase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vega.financial.calculator.OptionCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ResponseTransform {
    private final OptionCalculator calculator;

    public ResponseTransform(@Qualifier("blackScholes") OptionCalculator calculator) {
        this.calculator = calculator;
    }

    public List<StockOptionPurchaseResponse> mapStockOptionPurchases(List<StockOptionPurchase> purchases) {
        var result = new ArrayList<StockOptionPurchaseResponse>();

        for (var purchase : purchases) {
            result.add(new StockOptionPurchaseResponse(calculator, purchase));
        }

        return result;
    }
}
